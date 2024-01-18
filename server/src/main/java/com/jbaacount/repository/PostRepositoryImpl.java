package com.jbaacount.repository;

import com.jbaacount.mapper.PostMapper;
import com.jbaacount.model.Post;
import com.jbaacount.payload.response.PostMultiResponse;
import com.jbaacount.payload.response.PostResponseForProfile;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jbaacount.model.QPost.post;
import static com.jbaacount.service.UtilService.calculateTime;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<PostMultiResponse> getPostsByBoardId(Long boardId, String keyword, Pageable pageable)
    {
        List<PostMultiResponse> data = new ArrayList<>();

        List<Post> result = query
                .select(post)
                .from(post)
                .where(post.board.id.eq(boardId))
                .where(titleCondition(keyword))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        for (Post post : result)
        {
            PostMultiResponse response = PostMapper.INSTANCE.toPostMultiResponse(post);
            response.setCommentsCount(post.getComments().size());
            response.setTimeInfo(calculateTime(post.getCreatedAt()));
            data.add(response);
        }

        JPAQuery<Long> count = query
                .select(post.count())
                .from(post)
                .where(titleCondition(keyword))
                .where(post.board.id.eq(boardId));

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
    }

    @Override
    public Page<PostMultiResponse> getPostsByCategoryId(Long categoryId, String keyword, Pageable pageable)
    {
        List<PostMultiResponse> data = query
                .select(extractPostResponse())
                .from(post)
                .where(post.category.id.eq(categoryId))
                .where(titleCondition(keyword))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        calculateTimeInfo(data);

        JPAQuery<Long> count = query
                .select(post.count())
                .from(post)
                .where(titleCondition(keyword))
                .where(post.category.id.eq(categoryId));

        return PageableExecutionUtils.getPage(data, pageable, count::fetchOne);
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

        for (PostResponseForProfile response : data)
        {
            response.setTimeInfo(calculateTime(response.getCreatedAt()));
        }

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
                post.category.id,
                post.category.name,
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

    private static void calculateTimeInfo(List<PostMultiResponse> data)
    {
        for (PostMultiResponse datum : data)
        {
            datum.setTimeInfo(calculateTime(datum.getCreatedAt()));
        }
    }

}
