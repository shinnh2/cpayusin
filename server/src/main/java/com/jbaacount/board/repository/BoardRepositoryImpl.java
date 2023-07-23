package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardInfoForResponse;
import com.jbaacount.board.dto.response.BoardResponseWithCategory;
import com.jbaacount.category.dto.response.CategoryInfoForResponse;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.QCategory;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.jbaacount.post.repository.PostRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.board.entity.QBoard.board;
import static com.jbaacount.category.entity.QCategory.category;
import static com.jbaacount.post.entity.QPost.post;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PostRepository postRepository;
    @Override
    public BoardInfoForResponse getBoardWithAllPostsInfo(Long boardId, Pageable pageable)
    {
        BoardInfoForResponse boardResult = query
                .select(extractBoardWithPostsInfo())
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();

        Page<PostInfoForResponse> postResult = postRepository.getAllPostsInfoForBoard(boardId, pageable);
        List<CategoryResponseDto> categoryList = getCategoryResponseList(boardId);

        PageDto<PostInfoForResponse> posts = new PageDto<>(postResult);
        boardResult.setCategory(categoryList);
        boardResult.setPosts(posts);
        return boardResult;
    }

    private List<CategoryResponseDto> getCategoryResponseList(Long boardId)
    {
        List<CategoryResponseDto> categoryList = query
                .select(extractCategoryInfo())
                .from(category)
                .where(category.board.id.eq(boardId))
                .fetch();
        return categoryList;
    }


    private ConstructorExpression<BoardInfoForResponse> extractBoardWithPostsInfo()
    {
        return Projections.constructor(BoardInfoForResponse.class,
                board.id,
                board.name);
    }

    private ConstructorExpression<CategoryResponseDto> extractCategoryInfo()
    {
        return Projections.constructor(CategoryResponseDto.class,
                category.id,
                category.name);
    }
}
