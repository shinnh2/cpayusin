package com.jbaacount.controller;

import com.jbaacount.global.security.userdetails.MemberDetails;
import com.jbaacount.model.Member;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/vote")
@RestController
public class VoteController
{
    private final VoteService voteService;

    @PostMapping("/post")
    public ResponseEntity<?> votePost(@AuthenticationPrincipal MemberDetails currentMember,
                                      @RequestParam(name = "id") Long postId) throws InterruptedException
    {
        boolean response = voteService.votePost(currentMember.getMember(), postId);
        if(response)
            return ResponseEntity.ok(new GlobalResponse<>("좋아요 성공"));

        else
            return ResponseEntity.ok(new GlobalResponse<>("좋아요 취소"));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> voteComment(@AuthenticationPrincipal MemberDetails currentMember,
                                         @RequestParam(name = "id") Long commentId) throws InterruptedException
    {
        boolean response = voteService.voteComment(currentMember.getMember(), commentId);

        if(response)
            return ResponseEntity.ok(new GlobalResponse<>("좋아요 성공"));

        else
            return ResponseEntity.ok(new GlobalResponse<>("좋아요 취소"));
    }
}
