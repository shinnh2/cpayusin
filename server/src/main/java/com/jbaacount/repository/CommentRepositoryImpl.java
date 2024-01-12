package com.jbaacount.repository;

import com.jbaacount.payload.response.CommentMultiResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jbaacount.model.QComment.comment;
import static com.jbaacount.model.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public List<CommentMultiResponse> getAllComments(Long postId)
    {
        List<CommentMultiResponse> listComments = query
                .select(extractCommentDto())
                .from(comment)
                .leftJoin(comment.parent)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdAt.asc())
                .fetch();


        Map<Long, CommentMultiResponse> commentMap = new HashMap<>();
        List<CommentMultiResponse> parentComments = new ArrayList<>();

        for (CommentMultiResponse comment : listComments)
        {
            commentMap.put(comment.getId(), comment);

            if(comment.getParentId() == null)
                parentComments.add(comment);

            else
            {
                CommentMultiResponse parentComment = commentMap.get(comment.getParentId());
                if(parentComment != null)
                    parentComment.getChildren().add(comment);
            }

        }

        return parentComments;
    }
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
                comment.voteCount,
                comment.createdAt);
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

    private ConstructorExpression<MemberSimpleResponse> extractMemberInfo()
    {
        return Projections.constructor(MemberSimpleResponse.class,
                member.id,
                member.nickname);
    }
}
