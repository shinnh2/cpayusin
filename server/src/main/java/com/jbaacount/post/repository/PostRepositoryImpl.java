package com.jbaacount.post.repository;

import com.jbaacount.global.utils.PaginationUtils;
import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import com.jbaacount.post.dto.response.PostResponseForProfile;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.jbaacount.member.entity.QMember.member;
import static com.jbaacount.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PaginationUtils paginationUtils;

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

    @Override
    public Slice<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Long last, Pageable pageable)
    {
        List<PostResponseForProfile> fetch = query
                .select(extractPostsForProfile())
                .from(post)
                .where(post.member.id.eq(memberId))
                .where(ltPostId(last))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return paginationUtils.toSlice(pageable, fetch);
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

    private ConstructorExpression<PostResponseForProfile> extractPostsForProfile()
    {
        return Projections.constructor(PostResponseForProfile.class,
                post.id,
                post.title,
                post.createdAt);
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

    private BooleanExpression ltPostId(Long postId)
    {
        return postId != null ? post.id.lt(postId) : null;
    }
}
