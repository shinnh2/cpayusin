package com.jbaacount.comment.repository;

import com.jbaacount.comment.dto.response.CommentMultiResponse;
import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.entity.QMember;
import com.jbaacount.vote.entity.Vote;
import com.jbaacount.vote.repository.VoteRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.jbaacount.comment.entity.QComment.comment;
import static com.jbaacount.member.entity.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom
{
    private final JPAQueryFactory query;
    private final VoteRepository voteRepository;

    @Override
    public Page<CommentMultiResponse> getAllComments(Long postId, Pageable pageable, Member member)
    {
        List<CommentMultiResponse> fetch = query
                .select(extractCommentDto())
                .from(comment)
                .leftJoin(comment.parent)
                .join(comment.member, QMember.member)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Map<Long, CommentMultiResponse> commentMap = new HashMap<>();
        List<CommentMultiResponse> rootComments = new ArrayList<>();

        for (CommentMultiResponse comment : fetch)
        {
            comment.setVoteStatus(checkMemberVotedCommentOrNot(member, comment.getId()));
            commentMap.put(comment.getId(), comment);

            if(comment.getParentId() == null)
                rootComments.add(comment);

            else
            {
                CommentMultiResponse parentComment = commentMap.get(comment.getParentId());
                if(parentComment != null)
                    parentComment.getChildren().add(comment);
            }

        }

        return new PageImpl<>(rootComments, pageable, rootComments.size());
    }

    ConstructorExpression<CommentMultiResponse> extractCommentDto()
    {
        return Projections.constructor(CommentMultiResponse.class,
                comment.id,
                comment.parent.id,
                comment.text,
                comment.voteCount,
                comment.createdAt,
                extractMemberInfo());
    }

    private ConstructorExpression<MemberInfoForResponse> extractMemberInfo()
    {
        return Projections.constructor(MemberInfoForResponse.class,
                member.id,
                member.nickname);
    }

    public boolean checkMemberVotedCommentOrNot(Member member, Long commentId)
    {
        if(member == null)
            return false;

        boolean voteStatus;
        Optional<Vote> optionalVote = voteRepository.checkMemberVotedCommentOrNot(member.getId(), commentId);
        voteStatus = optionalVote.isPresent();

        return voteStatus;
    }
}
