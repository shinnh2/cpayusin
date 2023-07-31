package com.jbaacount.comment.service;

import com.jbaacount.comment.dto.request.CommentPatchDto;
import com.jbaacount.comment.dto.response.CommentMultiResponse;
import com.jbaacount.comment.entity.Comment;
import com.jbaacount.comment.repository.CommentRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorizationService authService;

    public Comment saveComment(Comment comment, Long postId, Long parentId, Member currentMember)
    {
        Post post = verifyPost(postId);
        Comment savedComment = commentRepository.save(comment);

        savedComment.addPost(post);
        savedComment.addMember(currentMember);
        if(parentId != null)
        {
            Comment parent = getComment(parentId);
            if(parent.getParent() != null)
            {
                throw new RuntimeException();
            }

            savedComment.addParent(parent);
        }
        return savedComment;
    }

    public Comment updateComment(CommentPatchDto request, Long postId, Long commentId, Member currentMember)
    {
        verifyPost(postId);
        Comment comment = getComment(commentId);
        authService.isTheSameUser(comment.getMember().getId(), currentMember.getId());

        Optional.ofNullable(request.getText())
                .ifPresent(text -> comment.updateText(text));

        return comment;
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId)
    {
        return commentRepository.findById(commentId).orElseThrow();
    }


    @Transactional(readOnly = true)
    public Page<CommentMultiResponse> getAllComments(Long postId, Member currentMember, Pageable pageable)
    {
        return commentRepository.getAllComments(postId, pageable, currentMember);
    }

    public void deleteComment(Long commentId, Member currentMember)
    {
        Comment comment = getComment(commentId);

        authService.checkPermission(comment.getMember().getId(), currentMember);

        if(comment.getChildren().isEmpty())
            commentRepository.deleteById(commentId);

        else
            comment.deleteComment();
    }

    private Post verifyPost(Long postId)
    {
        return postRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.COMMENT_NOT_FOUND));
    }

}
