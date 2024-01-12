package com.jbaacount.service;

import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.model.Vote;
import com.jbaacount.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
            post.getMember().getScoreByVote();

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
            comment.getMember().getScoreByVote();

            log.info("===voteService===");
            log.info("vote saved successfully");
            log.info("voted comment = {}", comment.getText());

            return true;
        }
    }

    @Transactional
    public void deleteVoteByPostId(Long postId)
    {
        voteRepository.deleteVoteByPostId(postId);
    }

    @Transactional
    public void deleteVoteByCommentId(Long commentId)
    {
        voteRepository.deleteVoteByCommentId(commentId);
    }


    public boolean existByMemberAndPost(Member member, Post post)
    {
        return voteRepository.existsVoteByMemberAndPost(member, post);
    }

    public boolean existByMemberAndComment(Long memberId, Long commentId)
    {
        return voteRepository.existsVoteByMemberIdAndCommentId(memberId, commentId);
    }
}
