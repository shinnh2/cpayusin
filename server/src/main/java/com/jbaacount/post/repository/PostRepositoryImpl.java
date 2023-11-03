package com.jbaacount.post.repository;

import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostMultiResponseDto;
import com.jbaacount.post.dto.response.PostResponseForProfile;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom
{
    private final JPAQueryFactory query;
    //private final PaginationUtils paginationUtils;

    /*@Override
    public SliceDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Long last, Pageable pageable)
    {
        List<PostResponseForProfile> fetch = query
                .select(extractPostsForProfile())
                .from(post)
                .where(post.member.id.eq(memberId))
                .where(ltPostId(last))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Slice<PostResponseForProfile> slice = paginationUtils.toSlice(pageable, fetch);

        return new SliceDto<>(fetch, slice);
    }*/

    /*@Override
    public SliceDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Long last, Pageable pageable)
    {
        List<PostMultiResponseDto> posts = query
                .select(extractPostsForCategoryAndBoard())
                .from(post)
                .where(post.category.id.eq(categoryId))
                .where(ltPostId(last))
                .where(titleCondition(keyword))
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.id.desc())
                .fetch();

        Slice<PostMultiResponseDto> slice = paginationUtils.toSlice(pageable, posts);

        return new SliceDto<>(posts, slice);
    }*/

    @Override
    public PageDto<PostResponseForProfile> getAllPostsByMemberId(Long memberId, Pageable pageable)
    {
        List<PostResponseForProfile> content = query
                .select(extractPostsForProfile())
                .from(post)
                .where(post.member.id.eq(memberId))
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Long total = query
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();

        PageImpl<PostResponseForProfile> pageDto = new PageImpl<>(content, pageable, total);

        return new PageDto<>(pageDto);
    }

    @Override
    public PageDto<PostMultiResponseDto> getAllPostsByCategoryId(Long categoryId, String keyword, Pageable pageable)
    {
        List<PostMultiResponseDto> content = query
                .select(extractPostsForCategoryAndBoard())
                .from(post)
                .where(post.category.id.eq(categoryId))
                .where(titleCondition(keyword))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.id.desc())
                .fetch();

        Long total = query
                .select(post.count())
                .from(post)
                .where(post.category.id.eq(categoryId))
                .where(titleCondition(keyword))
                .fetchOne();

        PageImpl<PostMultiResponseDto> pageDto = new PageImpl<>(content, pageable, total);

        return new PageDto<>(pageDto);

    }

    @Override
    public PageDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Pageable pageable)
    {
        List<PostMultiResponseDto> content = query
                .select(extractPostsForCategoryAndBoard())
                .from(post)
                .where(post.board.id.eq(boardId))
                .where(titleCondition(keyword))
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber())
                .orderBy(post.id.desc())
                .fetch();

        Long total = query
                .select(post.count())
                .from(post)
                .where(post.board.id.eq(boardId))
                .where(titleCondition(keyword))
                .fetchOne();

        PageImpl<PostMultiResponseDto> pageDto = new PageImpl<>(content, pageable, total);

        return new PageDto<>(pageDto);
    }

    /*@Override
    public SliceDto<PostMultiResponseDto> getAllPostsByBoardId(Long boardId, String keyword, Long last, Pageable pageable)
    {
        List<PostMultiResponseDto> posts = query
                .select(extractPostsForCategoryAndBoard())
                .from(post)
                .where(post.board.id.eq(boardId))
                .where(ltPostId(last))
                .where(titleCondition(keyword))
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.id.desc())
                .fetch();

        Slice<PostMultiResponseDto> slice = paginationUtils.toSlice(pageable, posts);

        return new SliceDto<>(posts, slice);
    }*/

    private ConstructorExpression<PostMultiResponseDto> extractPostsForCategoryAndBoard()
    {
        return Projections.constructor(PostMultiResponseDto.class,
                extractBoardResponse(),
                extractCategoryResponse(),
                extractMemberResponse(),
                post.id,
                post.title,
                post.content,
                post.voteCount,
                post.comments.size(),
                post.createdAt);
    }
    private ConstructorExpression<PostMultiResponseDto.BoardResponse> extractBoardResponse()
    {
        return Projections.constructor(PostMultiResponseDto.BoardResponse.class,
                post.board.id,
                post.board.name);
    }

    private ConstructorExpression<PostMultiResponseDto.CategoryResponse> extractCategoryResponse()
    {
        return Projections.constructor(PostMultiResponseDto.CategoryResponse.class,
                post.category.id,
                post.category.name);
    }
    private ConstructorExpression<PostMultiResponseDto.MemberResponse> extractMemberResponse()
    {
        return Projections.constructor(PostMultiResponseDto.MemberResponse.class,
                post.member.id,
                post.member.nickname);
    }


    private ConstructorExpression<PostResponseForProfile> extractPostsForProfile()
    {
        return Projections.constructor(PostResponseForProfile.class,
                post.id,
                post.title,
                post.createdAt);
    }

    private BooleanExpression titleCondition(String keyword)
    {
        return keyword != null ? post.title.lower().contains(keyword.toLowerCase()) : null;
    }

    private BooleanExpression ltPostId(Long postId)
    {
        return postId != null ? post.id.lt(postId) : null;
    }
}
