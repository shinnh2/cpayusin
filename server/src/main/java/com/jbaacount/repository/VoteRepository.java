package com.jbaacount.repository;

import com.jbaacount.model.Comment;
import com.jbaacount.model.Member;
import com.jbaacount.model.Post;
import com.jbaacount.model.Vote;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    Optional<Vote> checkMemberVotedPostOrNot(Member member, Post post);

    @Query("select v from Vote v where v.member.id = :memberId and v.comment.id = :commentId")
    Optional<Vote> checkMemberVotedCommentOrNot(Long memberId, Long commentId);

    @Transactional
    @Modifying
    void deleteByPostId(Long postId);

    void deleteVoteByPostId(@Param("postId") Long postId);

    void deleteVoteByCommentId(@Param("commentId") Long commentId);

    @Transactional
    @Modifying
    void deleteByCommentId(Long commentId);

    boolean existsVoteByMemberAndPost(Member member, Post post);

    boolean existsVoteByMemberAndComment(Member member, Comment comment);

    @Query("SELECT COUNT(*) FROM Vote v " +
            "WHERE v.member.id = :memberId AND v.post.id = :postId")
    boolean existsVoteByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Query("SELECT COUNT(*) FROM Vote v " +
            "WHERE v.member.id = :memberId AND v.comment.id = :commentId")
    boolean existsVoteByMemberIdAndCommentId(@Param("memberId") Long memberId, @Param("commentId") Long commentId);
}
