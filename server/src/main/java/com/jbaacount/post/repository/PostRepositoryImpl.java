package com.jbaacount.post.repository;

import com.jbaacount.post.dto.response.PostInfoForCategory;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<PostInfoForCategory> getAllPostsForCategory(Long categoryId, Pageable pageable)
    {
        List<PostInfoForCategory> postInfoResult = query
                .select(getPosts())
                .from(post)
                .where(post.category.id.eq(categoryId))
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        return new PageImpl<>(postInfoResult, pageable, postInfoResult.size());
    }

    public ConstructorExpression<PostInfoForCategory> getPosts()
    {
        return Projections.constructor(PostInfoForCategory.class,
                post.id,
                post.category.name,
                post.title,
                post.member.nickname,
                post.createdAt);
    }
}
