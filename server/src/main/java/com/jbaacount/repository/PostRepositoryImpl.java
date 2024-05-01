package com.jbaacount.repository;

import com.jbaacount.model.Post;
import com.jbaacount.payload.response.post.PostMultiResponse;
import com.jbaacount.payload.response.post.PostResponseForProfile;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.jbaacount.model.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<Post> getPostsByBoardIds(List<Long> boardIds, String keyword, Pageable pageable)
    {
        List<Post> result = query
                .select(post)
                .from(post)
                .where(post.board.id.in(boardIds))
                .where(titleCondition(keyword))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> count = query
                .select(post.count())
                .from(post)
                .where(titleCondition(keyword))
                .where(post.board.id.in(boardIds));

        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }

    @Override
    public Page<PostResponseForProfile> getPostsByMemberId(Long memberId, Pageable pageable)
    {
        List<PostResponseForProfile> data = query
                .select(extractPostsForProfile())
                .from(post)
                .where(post.member.id.eq(memberId))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> count = query
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId));


        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    private ConstructorExpression<PostResponseForProfile> extractPostsForProfile()
    {
        return Projections.constructor(PostResponseForProfile.class,
                post.id,
                post.title,
                post.createdAt);
    }

    private ConstructorExpression<PostMultiResponse> extractPostResponse()
    {
        return Projections.constructor(PostMultiResponse.class,
                post.member.id,
                post.member.nickname,
                post.board.id,
                post.board.name,
                post.id,
                post.title,
                post.content,
                post.comments.size(),
                post.createdAt);
    }


    private BooleanExpression titleCondition(String keyword)
    {
        return keyword != null ? post.title.lower().contains(keyword.toLowerCase()) : null;
    }

}
