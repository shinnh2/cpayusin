package com.jbaacount.vote.service;

import com.jbaacount.comment.entity.Comment;
import com.jbaacount.comment.service.CommentService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VoteFacade
{
    private final VoteService voteService;
    private final PostService postService;
    private final CommentService commentService;

    public boolean votePost(Member currentMember, Long postId) throws InterruptedException
    {
        Post post = postService.getPostById(postId);
        int retryCount = 0;
        int maxRetries = 100;

        while(retryCount < maxRetries)
        {
            try{
                log.info("vote post succeeded = {}", currentMember.getNickname());
                return voteService.votePost(currentMember, post);
            } catch (RuntimeException e){
                retryCount++;
                log.info("vote post failed = {}", currentMember.getNickname());
                Thread.sleep(10);
            }
        } 

        throw new RuntimeException("잠시 후 다시 시도해주세요");
    }

    public boolean voteComment(Member currentMember, Long commentId) throws InterruptedException
    {
        Comment comment = commentService.getComment(commentId);
        int retryCount = 0;
        int maxRetries = 100;

        while(retryCount < maxRetries)
        {
            try{
                return voteService.voteComment(currentMember, comment);
            } catch (RuntimeException e){
                retryCount++;
                Thread.sleep(10);
            }
        }

        throw new RuntimeException("잠시 후 다시 시도해주세요");
    }
}
