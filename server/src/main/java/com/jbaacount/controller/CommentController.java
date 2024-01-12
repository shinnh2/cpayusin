package com.jbaacount.controller;

import com.jbaacount.global.dto.PageInfo;
import com.jbaacount.mapper.CommentMapper;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.request.CommentPatchDto;
import com.jbaacount.payload.response.CommentMultiResponse;
import com.jbaacount.payload.response.CommentResponseForProfile;
import com.jbaacount.payload.response.CommentSingleResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentController
{
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PostMapping("/{post-id}/comment")
    public ResponseEntity<GlobalResponse<CommentSingleResponse>> saveComment(@PathVariable("post-id") @Positive Long postId,
                                      @RequestBody @Valid CommentCreateRequest request,
                                      @AuthenticationPrincipal Member member)
    {

        var data = commentService.saveComment(request, postId, request.getParentId(), member);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/comment/{comment-id}")
    public ResponseEntity updateComment(@RequestBody @Valid CommentPatchDto request,
                                        @PathVariable("comment-id") @Positive Long commentId,
                                        @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.updateComment(request, commentId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/comment/{comment-id}")
    public ResponseEntity getComment(@PathVariable("comment-id") @Positive Long commentId,
                                     @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.getCommentSingleResponse(commentId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/{post-id}/comments")
    public ResponseEntity<GlobalResponse<List<CommentMultiResponse>>> getAllComments(@PathVariable("post-id") @Positive Long postId,
                                                                                     @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.getAllComments(postId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/profile/{member-id}/comments")
    public ResponseEntity<GlobalResponse<List<CommentResponseForProfile>>> getAllCommentsForProfile(@PathVariable("member-id") @Positive Long memberId,
                                                                                                    @PageableDefault(size = 8) Pageable pageable)
    {
        var data = commentService.getAllCommentsForProfile(memberId, pageable.previousOrFirst());

        return ResponseEntity.ok(new GlobalResponse<>(data.getContent(), PageInfo.of(data)));
    }


    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive Long commentId,
                                        @AuthenticationPrincipal Member currentMember)
    {
        commentService.deleteComment(commentId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
