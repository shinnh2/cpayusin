package com.jbaacount.vote.service;

import com.jbaacount.comment.entity.Comment;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.vote.entity.Vote;
import com.jbaacount.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class VoteService
{
    private final VoteRepository voteRepository;
    public boolean votePost(Member currentMember, Post post)
    {
        Optional<Vote> optionalVote = voteRepository.findByMemberAndPost(currentMember, post);

        if(optionalVote.isPresent())
        {
            voteRepository.delete(optionalVote.get());
            return false;
        }

        else
        {
            voteRepository.save(new Vote(currentMember, post));
            return true;
        }
    }

    public boolean voteComment(Member currentMember, Comment comment)
    {
        Optional<Vote> optionalVote = voteRepository.findByMemberAndComment(currentMember, comment);

        if(optionalVote.isPresent())
        {
            voteRepository.delete(optionalVote.get());
            return false;
        }

        else
        {
            voteRepository.save(new Vote(currentMember, comment));
            return true;
        }
    }
}
