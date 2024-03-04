package com.jbaacount.comment.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.PostCreateRequest;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.CommentSingleResponse;
import com.jbaacount.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class CommentServiceTest
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

    private static final String ADMIN_EMAIL = "mike@ticonsys.com";

    @BeforeEach
    void beforeEach()
    {
        MemberRegisterRequest admin = new MemberRegisterRequest();
        admin.setEmail("mike@ticonsys.com");
        admin.setNickname("운영자");
        admin.setPassword("123123123");


        BoardCreateRequest request1 = new BoardCreateRequest();
        request1.setName("게시판1");
        request1.setIsAdminOnly(true);

        authService.register(admin);

        BoardResponse board = boardService.createBoard(request1, memberService.findMemberByEmail("mike@ticonsys.com"));


        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setTitle("게시글 제목");
        postCreateRequest.setContent("게시글 내용");
        postCreateRequest.setBoardId(board.getId());


        postService.createPost(postCreateRequest, null, memberService.findMemberByEmail(admin.getEmail()));
    }


    @DisplayName("댓글 1개 등록")
    @Test
    void createComment()
    {
        Member admin = findMemberByEmail(ADMIN_EMAIL);
        String commentText = "댓글 제목";

        CommentSingleResponse response = saveComment(admin, commentText, null);

        assertThat(response.getText()).isEqualTo("댓글 제목");
        assertThat(response.getParentId()).isNull();
        assertThat(response.getMemberId()).isEqualTo(admin.getId());
    }

    @DisplayName("댓글과 대댓글 1개씩 등록")
    @Test
    void createChildComment()
    {
        Member admin = findMemberByEmail(ADMIN_EMAIL);
        List<Post> posts = admin.getPosts();
        Post post = posts.get(0);

        String parentCommentText = "댓글 내용";
        String childCommentText = "대댓글 내용";


        CommentSingleResponse parentComment = saveComment(admin, parentCommentText, null);
        CommentSingleResponse childComment = saveComment(admin, childCommentText, parentComment.getCommentId());


        assertThat(childComment.getParentId()).isEqualTo(parentComment.getCommentId());
        assertThat(childComment.getText()).isEqualTo(childCommentText);
        assertThat(childComment.getNickname()).isEqualTo(admin.getNickname());

        assertThat(parentComment.getParentId()).isNull();
        assertThat(parentComment.getText()).isEqualTo(parentCommentText);
        assertThat(parentComment.getVoteCount()).isEqualTo(0);
    }


    @DisplayName("대댓글이 없는 댓글 삭제")
    @Test
    void deleteComment()
    {
        Member admin = findMemberByEmail(ADMIN_EMAIL);

        CommentSingleResponse response = saveComment(admin, "댓글", null);

        commentService.deleteComment(response.getCommentId(), admin);

        assertThrows(BusinessLogicException.class, () -> {
            commentService.getComment(response.getCommentId());
        });
    }


    @DisplayName("대댓글이 있는 댓글은 삭제되지 않는다")
    @Test
    void deleteCommentThatHasChildComment()
    {
        Member admin = findMemberByEmail(ADMIN_EMAIL);

        CommentSingleResponse response = saveComment(admin, "댓글", null);
        CommentSingleResponse childResponse = saveComment(admin, "대댓글", response.getCommentId());

        commentService.deleteComment(response.getCommentId(), admin);


        Comment removedComment = commentService.getComment(response.getCommentId());
        Comment childComment = commentService.getComment(childResponse.getCommentId());


        assertThat(removedComment.isRemoved()).isTrue();
        assertThat(childComment.isRemoved()).isFalse();
        assertThat(childResponse.getText()).isEqualTo("대댓글");
    }


    public Member findMemberByEmail(String email)
    {
        return memberService.findMemberByEmail(email);
    }

    public CommentSingleResponse saveComment(Member member, String text, Long parentCommentId)
    {
        List<Post> posts = member.getPosts();
        Post post = posts.get(0);

        CommentCreateRequest request = new CommentCreateRequest();
        request.setText(text);
        request.setPostId(post.getId());
        request.setParentCommentId(parentCommentId);

        return commentService.saveComment(request, member);
    }
}
