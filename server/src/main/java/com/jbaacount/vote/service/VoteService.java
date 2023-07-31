package com.jbaacount.vote.service;

import com.jbaacount.comment.entity.Comment;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.vote.entity.Vote;
import com.jbaacount.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteService
{
    private final VoteRepository voteRepository;

    @Transactional
    public boolean votePost(Member currentMember, Post post)
    {
        Optional<Vote> optionalVote = voteRepository.findByMemberAndPost(currentMember, post);

        if(optionalVote.isPresent())
        {
            voteRepository.delete(optionalVote.get());
            log.info("===voteService===");
            log.info("vote deleted successfully");
            post.downVote();
            return false;
        }

        else
        {
            log.info("===voteService===");
            log.info("vote saved successfully");
            Vote savedVote = voteRepository.save(new Vote(currentMember, post));
            post.upVote();

            log.info("voted post = {}", savedVote.getPost().getTitle());
            return true;
        }
    }

    @Transactional
    public boolean voteComment(Member currentMember, Comment comment)
    {
        Optional<Vote> optionalVote = voteRepository.findByMemberAndComment(currentMember, comment);

        if(optionalVote.isPresent())
        {
            voteRepository.delete(optionalVote.get());
            comment.downVote();

            log.info("===voteService===");
            log.info("vote deleted successfully");

            return false;
        }

        else
        {
            voteRepository.save(new Vote(currentMember, comment));
            comment.upVote();

            log.info("===voteService===");
            log.info("vote saved successfully");
            log.info("voted comment = {}", comment.getText());

            return true;
        }
    }
}
