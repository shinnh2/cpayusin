package com.jbaacount.vote.controller;

import com.jbaacount.member.entity.Member;
import com.jbaacount.post.repository.PostRepository;
import com.jbaacount.post.service.PostService;
import com.jbaacount.vote.service.VoteFacade;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/vote")
@RestController
public class VoteController
{
    private final VoteFacade voteFacade;

    @PostMapping("/post/{post-id}")
    public ResponseEntity<?> votePost(@AuthenticationPrincipal Member currentMember,
                                      @PathVariable("post-id") @Positive Long postId) throws InterruptedException
    {
        boolean response = voteFacade.votePost(currentMember, postId);
        if(response)
            return new ResponseEntity<>("좋아요 성공", HttpStatus.CREATED);

        else
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
    }

    @PostMapping("/comment/{comment-id}")
    public ResponseEntity<?> voteComment(@AuthenticationPrincipal Member currentMember,
                                         @PathVariable("comment-id") @Positive Long commentId) throws InterruptedException
    {
        boolean response = voteFacade.voteComment(currentMember, commentId);

        if(response)
            return new ResponseEntity<>("좋아요 성공", HttpStatus.CREATED);

        else
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
    }
}
