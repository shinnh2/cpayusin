package com.jbaacount.comment.controller;

import com.jbaacount.comment.dto.request.CommentPatchDto;
import com.jbaacount.comment.dto.request.CommentPostDto;
import com.jbaacount.comment.dto.response.CommentSingleResponse;
import com.jbaacount.comment.entity.Comment;
import com.jbaacount.comment.mapper.CommentMapper;
import com.jbaacount.comment.service.CommentService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController
{
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PostMapping("/{post-id}/comment")
    public ResponseEntity saveComment(@PathVariable("post-id") @Positive Long postId,
                                      @RequestBody CommentPostDto request,
                                      @AuthenticationPrincipal Member currentMember)
    {
        Comment comment = commentMapper.postToComment(request);
        Comment savedComment = commentService.saveComment(comment, postId, request.getParentId(), currentMember);
        CommentSingleResponse response = commentMapper.commentToResponse(savedComment, currentMember);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{post-id}/comment/{comment-id}")
    public ResponseEntity updateComment(@PathVariable("post-id") @Positive Long postId,
                                        @RequestBody CommentPatchDto request,
                                        @PathVariable("comment-id") @Positive Long commentId,
                                        @AuthenticationPrincipal Member currentMember)
    {
        Comment updatedComment = commentService.updateComment(request, postId, commentId, currentMember);
        CommentSingleResponse response = commentMapper.commentToResponse(updatedComment, currentMember);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/comment/{comment-id}")
    public ResponseEntity getComment(@PathVariable("comment-id") @Positive Long commentId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        Comment comment = commentService.getComment(commentId);
        CommentSingleResponse response = commentMapper.commentToResponse(comment, currentMember);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive Long commentId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        commentService.deleteComment(commentId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
