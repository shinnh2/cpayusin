package com.jbaacount.post.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.post.dto.request.PostPatchDto;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import com.jbaacount.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class PostServiceTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    private static final String boardName = "첫번째 게시판";
    private static final String categoryName = "JPA란";
    private static final String post1Title = "게시판1";
    private static final String post2Title = "게시판2";

    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);
        Member user = TestUtil.createUserMember(memberService);

        System.out.println("admin nickname = " + admin.getNickname());

        Board board1 = Board.builder()
                .name(boardName)
                .isAdminOnly(false)
                .build();

        Board board2 = Board.builder()
                .name("게시판2")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board1, admin);
        boardService.createBoard(board2, admin);

        Category category = Category.builder()
                .name(categoryName)
                .isAdminOnly(false)
                .build();

        categoryService.createCategory(category, board1.getId(), admin);

        Post post1 = Post
                .builder()
                .title(post1Title)
                .content("내용 테스트용1")
                .build();

        Post post2 = Post
                .builder()
                .title(post2Title)
                .content("내용 테스트용2")
                .build();

        List<MultipartFile> files = new ArrayList<>();

        postService.createPost(post1, files, category.getId(), board1.getId(), user);
        postService.createPost(post2, files, null, board1.getId(), admin);

    }

    @DisplayName("게시글 생성 - 카테고리 X")
    @Test
    void createPost_isAdminFalse1()
    {
        Member user = getUser();
        String title = "첫번째게시물";
        String content = "내용";

        Board board = getBoard();
        Category category = getCategory();


        Post post = Post
                .builder()
                .title(title)
                .content(content)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        Post savedPost = postService.createPost(post, files, null, board.getId(), user);

        assertThat(savedPost.getTitle()).isEqualTo("첫번째게시물");
        assertThat(savedPost.getContent()).isEqualTo("내용");
        assertThat(savedPost.getCategory()).isNull();
    }

    @DisplayName("게시글 생성 - 카테고리 O")
    @Test
    void createPost_isAdminFalse2()
    {
        Member user = getUser();
        String title = "첫번째게시물";
        String content = "내용";

        Board board = getBoard();
        Category category = getCategory();


        Post post = Post
                .builder()
                .title(title)
                .content(content)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        Post savedPost = postService.createPost(post, files, category.getId(), board.getId(), user);

        assertThat(savedPost.getCategory()).isNotNull();
        assertThat(savedPost.getBoard()).isEqualTo(board);
    }

    @DisplayName("게시글 생성 - isAdminOnly : true")
    @Test
    void createPost_isAdminOnlyTrue()
    {
        Member user = getUser();

        Board board = getAdminOnlyBoard();

        Post post = Post
                .builder()
                .title("게시판1")
                .content("내용1")
                .build();

        List<MultipartFile> files = new ArrayList<>();
        assertThrows(BusinessLogicException.class, () -> postService.createPost(post, files, null, board.getId(), user));
    }


    @DisplayName("게시글 수정 - 해당 유저가 시도")
    @Test
    void updatePost_WithAuthorizedUser()
    {
        Member user = getUser();
        Post userPost = getPost1(post1Title);

        PostPatchDto patchDto = new PostPatchDto();
        patchDto.setTitle("제목 수정 테스트 1");

        List<MultipartFile> files = new ArrayList<>();
        Post updatedPost = postService.updatePost(userPost.getId(), patchDto, files, user);

        assertThat(updatedPost.getTitle()).isEqualTo("제목 수정 테스트 1");
        assertThat(updatedPost.getMember().getNickname()).isEqualTo(user.getNickname());
    }



    @DisplayName("게시글 수정 - 다른 사용자가 시도")
    @Test
    void updatePost_WithDifferentUser()
    {
        Member user = getUser();

        Post adminPost = getPost1(post2Title);

        PostPatchDto patchDto = new PostPatchDto();
        patchDto.setTitle("제목 수정 테스트 2");

        List<MultipartFile> files = new ArrayList<>();
        assertThrows(BusinessLogicException.class, () -> postService.updatePost(adminPost.getId(), patchDto, files, user));
    }

    @DisplayName("게시글 삭제 - 해당 유저가 시도")
    @Test
    void deletePost_WithAuthorizedUser()
    {
        Member user = getUser();

        Post userPost = postRepository.findByTitle(post1Title).get();
        postService.deletePostById(userPost.getId(), user);

        assertThat(postRepository.findByTitle(post1Title)).isEmpty();
    }

    @DisplayName("게시글 삭제 - 다른 사용자가 시도")
    @Test
    void deletePost_WithUnauthorizedUser()
    {
        Member user = getUser();
        Member admin = getAdmin();

        Post adminPost = postRepository.findByTitle(post2Title).get();

        assertThrows(BusinessLogicException.class, () -> postService.deletePostById(adminPost.getId(), user));
    }

    @DisplayName("게시글 삭제 - 관리자가 유저의 게시글 삭제")
    @Test
    void deletePost_WithAdmin()
    {
        Member admin = getAdmin();
        Member user = getUser();

        Post userPost = getPost1(post1Title);

        postService.deletePostById(userPost.getId(), admin);

        assertThat(postRepository.findByTitle(post1Title)).isEmpty();
    }


    private Post getPost1(String post1Title)
    {
        return postRepository.findByTitle(post1Title).get();
    }

    private Board getBoard()
    {
        return boardRepository.findBoardByName(boardName).get();
    }

    private Board getAdminOnlyBoard()
    {
        return boardRepository.findBoardByName("게시판2").get();
    }

    private Category getCategory()
    {
        return categoryRepository.findByName(categoryName).get();
    }

    private Member getAdmin()
    {
        return memberRepository.findByEmail(adminEmail).get();
    }

    private Member getUser()
    {
        return memberRepository.findByEmail(userEmail).get();
    }
}
