package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardWithAllCategoriesResponse;
import com.jbaacount.board.dto.response.BoardWithAllPostsResponse;
import com.jbaacount.category.dto.response.CategoryResponseForBoard;
import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import com.jbaacount.post.repository.PostRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.board.entity.QBoard.board;
import static com.jbaacount.category.entity.QCategory.category;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    private final PostRepository postRepository;

    @Override
    public BoardWithAllPostsResponse getBoardWithAllPostsInfo(Long boardId, Pageable pageable)
    {
        BoardWithAllPostsResponse boardResult = query
                .select(extractBoardWithPostsInfo())
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();

        Page<PostInfoForOtherResponse> postResult = postRepository.getAllPostsInfoForBoard(boardId, pageable);

        PageDto<PostInfoForOtherResponse> posts = new PageDto<>(postResult);
        boardResult.setPosts(posts);
        return boardResult;
    }


    @Override
    public BoardWithAllCategoriesResponse getBoardWithAllCatetoriesInfo(Long boardId)
    {
        BoardWithAllCategoriesResponse boardResult = query
                .select(extractBoardWithCategoriesInfo())
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();

        List<CategoryResponseForBoard> categories = getAllCategories(boardId);
        boardResult.setCategory(categories);

        return boardResult;
    }

    @Override
    public Page<BoardWithAllCategoriesResponse> getAllBoardsAndCategories(Pageable pageable)
    {
        List<BoardWithAllCategoriesResponse> boardResult = query
                .select(extractBoardWithCategoriesInfo())
                .from(board)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        for (BoardWithAllCategoriesResponse boardResponse : boardResult)
        {
            List<CategoryResponseForBoard> categories = getAllCategories(boardResponse.getId());
            boardResponse.setCategory(categories);
        }

        return new PageImpl<>(boardResult, pageable, boardResult.size());
    }

    private List<CategoryResponseForBoard> getAllCategories(Long boardId)
    {
        return query
                .select(extractCategoryInfo())
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(category.name.asc())
                .fetch();
    }

    private ConstructorExpression<BoardWithAllPostsResponse> extractBoardWithPostsInfo()
    {
        return Projections.constructor(BoardWithAllPostsResponse.class,
                board.id,
                board.name);
    }

    private ConstructorExpression<BoardWithAllCategoriesResponse> extractBoardWithCategoriesInfo()
    {
        return Projections.constructor(BoardWithAllCategoriesResponse.class,
                board.id,
                board.name);
    }

    private ConstructorExpression<CategoryResponseForBoard> extractCategoryInfo()
    {
        return Projections.constructor(CategoryResponseForBoard.class,
                category.id,
                category.name);
    }
}
