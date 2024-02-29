package com.jbaacount.post;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.request.PostUpdateRequest;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.PostResponseForProfile;
import com.jbaacount.payload.response.PostSingleResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.BoardService;
import com.jbaacount.service.MemberService;
import com.jbaacount.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class PostServiceTest
{
    @Autowired
    private BoardService boardService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @BeforeEach
    void beforeEach()
    {
        MemberRegisterRequest admin = new MemberRegisterRequest();
        admin.setEmail("mike@ticonsys.com");
        admin.setNickname("운영자");
        admin.setPassword("123123123");

        MemberRegisterRequest user = new MemberRegisterRequest();
        user.setEmail("user@naver.com");
        user.setNickname("유저");
        user.setPassword("123123123");

        String boardName1 = "게시판1";

        BoardCreateRequest request1 = new BoardCreateRequest();
        request1.setName(boardName1);
        request1.setIsAdminOnly(true);

        String boardName2 = "게시판2";
        BoardCreateRequest request2 = new BoardCreateRequest();
        request2.setName(boardName2);
        request2.setIsAdminOnly(false);

        authService.register(admin);
        authService.register(user);

        boardService.createBoard(request1, memberService.findMemberByEmail("mike@ticonsys.com"));
        boardService.createBoard(request2, memberService.findMemberByEmail("mike@ticonsys.com"));
    }


    @DisplayName("게시글 등록")
    @Test
    void createPost()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");

        List<BoardMenuResponse> menuList = boardService.getMenuList();
        BoardMenuResponse board = menuList.get(0);

        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("제목 테스트");
        request.setContent("내용 테스트");

        PostSingleResponse savedPost = postService.createPost(request, null, board.getId(), admin);

        assertThat(savedPost.getContent()).isEqualTo("내용 테스트");
        assertThat(savedPost.getTitle()).isEqualTo("제목 테스트");
        assertThat(savedPost.getMemberId()).isEqualTo(admin.getId());

    }

    @DisplayName("유저가 게시글을 등록")
    @Test
    void createPost_User()
    {
        Member user = memberService.findMemberByEmail("user@naver.com");

        String title = "제목 테스트";
        String content = "내용 테스트";

        PostSingleResponse savedPost = savePost(title, content, user, 1);

        assertThat(savedPost.getContent()).isEqualTo("내용 테스트");
        assertThat(savedPost.getTitle()).isEqualTo("제목 테스트");
        assertThat(savedPost.getMemberId()).isEqualTo(user.getId());

    }

    @DisplayName("유저가 관리자만 글을 쓸 수 있는 게시판에 글을 등록")
    @Test
    void createPost_IsAdminTrue_User()
    {
        Member user = memberService.findMemberByEmail("user@naver.com");

        List<BoardMenuResponse> menuList = boardService.getMenuList();
        BoardMenuResponse boardForOnlyAdmin = menuList.get(0);

        PostCreateRequest request = new PostCreateRequest();
        request.setTitle("제목 테스트");
        request.setContent("내용 테스트");

        assertThrows(BusinessLogicException.class, () -> {
            postService.createPost(request, null, boardForOnlyAdmin.getId(), user);
        });
    }

    @DisplayName("게시글 수정")
    @Test
    void updatePost()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");
        PostSingleResponse post = savePost("제목", "내용", admin, 0);

        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setTitle("제목-수정");
        updateRequest.setContent("내용-수정");

        PostSingleResponse updatedPost = postService.updatePost(post.getId(), updateRequest, null, admin);

        assertThat(updatedPost.getTitle()).isEqualTo("제목-수정");
        assertThat(updatedPost.getContent()).isEqualTo("내용-수정");
    }

    @DisplayName("다른 유저가 게시글을 수정")
    @Test
    void updatePost_byAnotherMember()
    {
        Member user = memberService.findMemberByEmail("user@naver.com");
        PostSingleResponse post = savePost("제목", "내용", user, 1);


        MemberRegisterRequest user2 = new MemberRegisterRequest();
        user2.setEmail("user2@naver.com");
        user2.setNickname("유저2");
        user2.setPassword("123123123");
        authService.register(user2);
        Member anotherMember = memberService.findMemberByEmail("user2@naver.com");


        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setTitle("제목-수정");
        updateRequest.setContent("내용-수정");


        assertThrows(BusinessLogicException.class, () -> {
            postService.updatePost(post.getId(), updateRequest, null, anotherMember);
        });
    }


    @Test
    void getMyPost()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");

        PostSingleResponse savedPost1 = savePost("제목 테스트1", "내용 테스트1", admin, 0);
        PostSingleResponse savedPost2 = savePost("제목 테스트2", "내용 테스트2", admin, 0);


        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponseForProfile> myPosts = postService.getMyPosts(admin, pageable);
        List<PostResponseForProfile> content = myPosts.getContent();

        //order by time desc

        assertThat(content.get(0).getTitle()).isEqualTo(savedPost2.getTitle());
        assertThat(content.get(1).getTitle()).isEqualTo(savedPost1.getTitle());
        assertThat(content.size()).isEqualTo(2);
    }

    @DisplayName("게시글 삭제")
    @Test
    void deletePost()
    {
        String email = "mike@ticonsys.com";
        Member admin = memberService.findMemberByEmail(email);

        PostSingleResponse post = savePost("제목", "내용", admin, 0);

        postService.deletePostById(post.getId(), admin);

        assertThrows(BusinessLogicException.class, () -> {
            postService.getPostById(post.getId());
        });
    }

    @DisplayName("일반 유저가 올린 게시글을 운영자가 삭제")
    @Test
    void deletePost_byAdmin()
    {
        String adminEmail = "mike@ticonsys.com";
        String userEmail = "user@naver.com";

        Member admin = memberService.findMemberByEmail(adminEmail);
        Member user = memberService.findMemberByEmail(userEmail);

        PostSingleResponse post = savePost("제목", "내용", user, 1);

        postService.deletePostById(post.getId(), admin);

        assertThrows(BusinessLogicException.class, () -> {
            postService.getPostById(post.getId());
        });
    }

    @DisplayName("일반 유저가 올린 글을 운영자가 아닌 다른 유저가 삭제")
    @Test
    void deletePost_byAnotherMember()
    {
        Member member = memberService.findMemberByEmail("user@naver.com");

        MemberRegisterRequest user2 = new MemberRegisterRequest();
        user2.setEmail("user2@naver.com");
        user2.setNickname("유저2");
        user2.setPassword("123123123");

        authService.register(user2);
        Member anotherMember = memberService.findMemberByEmail("user2@naver.com");


        PostSingleResponse post = savePost("제목", "내용", member, 1);

        assertThrows(BusinessLogicException.class, () -> {
            postService.deletePostById(post.getId(), anotherMember);
        });
    }



    PostSingleResponse savePost(String title, String content, Member member, int boardNum)
    {
        List<BoardMenuResponse> menuList = boardService.getMenuList();
        BoardMenuResponse board = menuList.get(boardNum);

        PostCreateRequest request1 = new PostCreateRequest();
        request1.setTitle(title);
        request1.setContent(content);
        return postService.createPost(request1, null, board.getId(), member);
    }

}
