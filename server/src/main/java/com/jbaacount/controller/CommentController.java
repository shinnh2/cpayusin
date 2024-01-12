package com.jbaacount.controller;

import com.jbaacount.global.dto.PageInfo;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.CommentCreateRequest;
import com.jbaacount.payload.request.CommentPatchDto;
import com.jbaacount.payload.response.CommentMultiResponse;
import com.jbaacount.payload.response.CommentResponseForProfile;
import com.jbaacount.payload.response.CommentSingleResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommentController
{
    private final CommentService commentService;

    @PostMapping("/comment/create")
    public ResponseEntity<GlobalResponse<CommentSingleResponse>> saveComment(@RequestParam("post") Long postId,
                                                                             @RequestBody @Valid CommentCreateRequest request,
                                                                             @RequestParam(required = false, name = "parent") Long parentId,
                                                                             @AuthenticationPrincipal Member member)
    {
        var data = commentService.saveComment(request, postId, parentId, member);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<GlobalResponse<CommentSingleResponse>> updateComment(@RequestBody @Valid CommentPatchDto request,
                                                                               @RequestParam("comment") Long commentId,
                                                                               @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.updateComment(request, commentId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/comment/{comment-id}")
    public ResponseEntity<GlobalResponse<CommentSingleResponse>> getComment(@PathVariable("comment-id") Long commentId,
                                                                            @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.getCommentSingleResponse(commentId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/comment")
    public ResponseEntity<GlobalResponse<List<CommentMultiResponse>>> getAllComments(@RequestParam("post") Long postId,
                                                                                     @AuthenticationPrincipal Member currentMember)
    {
        var data = commentService.getAllComments(postId, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/profile/my-comments")
    public ResponseEntity<GlobalResponse<List<CommentResponseForProfile>>> getAllCommentsForProfile(@AuthenticationPrincipal Member member,
                                                                                                    @PageableDefault(size = 8) Pageable pageable)
    {
        var data = commentService.getAllCommentsForProfile(member.getId(), pageable.previousOrFirst());

        return ResponseEntity.ok(new GlobalResponse<>(data.getContent(), PageInfo.of(data)));
    }


    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") Long commentId,
                                        @AuthenticationPrincipal Member currentMember)
    {
        commentService.deleteComment(commentId, currentMember);

        return ResponseEntity.ok(new GlobalResponse("댓글을 삭제했습니다."));
    }
}
