package com.jbaacount.vote.repository;

import com.jbaacount.comment.entity.Comment;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import com.jbaacount.vote.entity.Vote;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long>
{
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select v from Vote v where v.member = :member and v.post = :post")
    Optional<Vote> findByMemberAndPost(Member member, Post post);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select v from Vote v where v.member = :member and v.comment = :comment")
    Optional<Vote> findByMemberAndComment(Member member, Comment comment);

    @Query("select v from Vote v where v.member = :member and v.post = :post")
    Optional<Vote> checkMemberVotedOrNot(Member member, Post post);

    @Query("select v from Vote v where v.member = :member and v.comment = :comment")
    Optional<Vote> checkMemberVotedCommentOrNot(Member member, Comment comment);
}
