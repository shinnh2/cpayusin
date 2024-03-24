package com.jbaacount.repository;

import com.jbaacount.payload.response.CommentResponseForProfile;
import com.jbaacount.payload.response.MemberSimpleResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.jbaacount.model.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<CommentResponseForProfile> getAllCommentsForProfile(Long memberId, Pageable pageable)
    {
        List<CommentResponseForProfile> content = query
                .select(extractAllCommentsForProfile())
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(comment.id.desc())
                .fetch();

        JPAQuery<Long> count = query
                .select(comment.count())
                .from(comment)
                .where(comment.member.id.eq(memberId));


        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }


    ConstructorExpression<CommentResponseForProfile> extractAllCommentsForProfile()
    {
        return Projections.constructor(CommentResponseForProfile.class,
                comment.id,
                comment.post.id,
                comment.text,
                comment.votes.size(),
                comment.isRemoved,
                comment.createdAt);
    }


    private ConstructorExpression<MemberSimpleResponse> extractMemberInfo()
    {
        return Projections.constructor(MemberSimpleResponse.class,
                comment.member.id,
                comment.member.nickname);
    }
}
