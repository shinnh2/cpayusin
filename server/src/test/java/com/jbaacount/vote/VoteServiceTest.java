package com.jbaacount.vote;

import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.PostSingleResponse;
import com.jbaacount.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
public class VoteServiceTest
{
    @Autowired
    private BoardService boardService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    VoteService voteService;

    private static final String ADMIN_EMAIL = "mike@ticonsys.com";
    private static final String USER_EMAIL = "user@naver.com";


    @BeforeEach
    void beforeEach()
    {
        MemberRegisterRequest admin = new MemberRegisterRequest();
        admin.setEmail(ADMIN_EMAIL);
        admin.setNickname("운영자");
        admin.setPassword("123123123");
        authService.register(admin);

        MemberRegisterRequest user = new MemberRegisterRequest();
        user.setEmail(USER_EMAIL);
        user.setNickname("유저");
        user.setPassword("123123123");
        authService.register(user);


        BoardCreateRequest request1 = new BoardCreateRequest();
        request1.setName("게시판1");
        request1.setIsAdminOnly(true);
        BoardResponse board = boardService.createBoard(request1, memberService.findMemberByEmail("mike@ticonsys.com"));


        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setTitle("게시글 제목");
        postCreateRequest.setContent("게시글 내용");
        postCreateRequest.setBoardId(board.getId());


        Member member = memberService.findMemberByEmail(ADMIN_EMAIL);
        PostSingleResponse post = postService.createPost(postCreateRequest, null, member);

        CommentCreateRequest comment = new CommentCreateRequest();
        comment.setText("댓글");
        comment.setPostId(post.getId());

        commentService.saveComment(comment, member);
    }


    @DisplayName("유저 2명이 투표")
    @Test
    void upVote()
    {
        Member admin = memberService.findMemberByEmail(ADMIN_EMAIL);
        Member user = memberService.findMemberByEmail(USER_EMAIL);

        List<Post> posts = admin.getPosts();
        Post post = posts.get(0);

        voteService.votePost(admin, post.getId());
        voteService.votePost(user, post.getId());

        Post votedPost = postService.getPostById(post.getId());

        assertThat(votedPost.getVoteCount()).isEqualTo(2);

    }


    @DisplayName("투표 취소")
    @Test
    void downVote()
    {
        Member admin = memberService.findMemberByEmail(ADMIN_EMAIL);
        Member user = memberService.findMemberByEmail(USER_EMAIL);

        List<Post> posts = admin.getPosts();
        Post post = posts.get(0);

        voteService.votePost(admin, post.getId());
        voteService.votePost(user, post.getId());

        Post votedPost = postService.getPostById(post.getId());

        assertThat(votedPost.getVoteCount()).isEqualTo(2);

        voteService.votePost(admin, post.getId());
        voteService.votePost(user, post.getId());

        votedPost = postService.getPostById(post.getId());

        assertThat(votedPost.getVoteCount()).isEqualTo(0);
    }


}
