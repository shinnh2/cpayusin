package com.jbaacount.post.repository;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.member.entity.QMember.member;
import static com.jbaacount.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<PostInfoForOtherResponse> getAllPostsInfoForCategory(Long categoryId, Pageable pageable)
    {
        List<PostInfoForOtherResponse> postInfoResult = query
                .select(extractPostsInfo())
                .from(post)
                .join(post.member, member)
                .where(post.category.id.eq(categoryId))
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        return new PageImpl<>(postInfoResult, pageable, postInfoResult.size());
    }

    public Page<PostInfoForOtherResponse> getAllPostsInfoForBoard(Long boardId, Pageable pageable)
    {
        List<PostInfoForOtherResponse> postInfoResult = query
                .select(extractPostsInfo())
                .from(post)
                .join(post.member, member)
                .where(post.board.id.eq(boardId))
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(postInfoResult, pageable, postInfoResult.size());
    }

    private ConstructorExpression<PostInfoForOtherResponse> extractPostsInfo()
    {
        return Projections.constructor(PostInfoForOtherResponse.class,
                post.id,
                post.title,
                post.createdAt,
                extractMemberInfo());
    }

    private ConstructorExpression<MemberInfoForResponse> extractMemberInfo()
    {
        return Projections.constructor(MemberInfoForResponse.class,
                member.id,
                member.nickname);
    }
}
