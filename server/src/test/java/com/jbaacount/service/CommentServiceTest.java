package com.jbaacount.service;

import com.jbaacount.Setup;
import com.jbaacount.mapper.CommentMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.comment.CommentCreateRequest;
import com.jbaacount.payload.response.comment.CommentCreatedResponse;
import com.jbaacount.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest extends Setup
{
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @Mock
    private UtilService authService;
    @Mock
    private VoteService voteService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void saveComment()
    {
        // given
        CommentCreateRequest request = new CommentCreateRequest();
        request.setText(mockComment.getText());
        request.setPostId(mockPost.getId());

        given(postService.getPostById(any())).willReturn(mockPost);
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);


        // when
        CommentCreatedResponse response = commentService.saveComment(request, mockMember);
        System.out.println("response = " + response);

        // then
        assertThat(response.getText()).isEqualTo(mockComment.getText());
    }
}