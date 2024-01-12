package com.jbaacount.controller;

import com.jbaacount.model.Member;
import com.jbaacount.service.VoteFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final VoteFacade voteFacade;

    @PostMapping("/post")
    public ResponseEntity<?> votePost(@AuthenticationPrincipal Member currentMember,
                                      @RequestParam(name = "id") Long postId) throws InterruptedException
    {
        boolean response = voteFacade.votePost(currentMember, postId);
        if(response)
            return new ResponseEntity<>("좋아요 성공", HttpStatus.CREATED);

        else
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> voteComment(@AuthenticationPrincipal Member currentMember,
                                         @RequestParam(name = "id") Long commentId) throws InterruptedException
    {
        boolean response = voteFacade.voteComment(currentMember, commentId);

        if(response)
            return new ResponseEntity<>("좋아요 성공", HttpStatus.CREATED);

        else
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
    }
}
