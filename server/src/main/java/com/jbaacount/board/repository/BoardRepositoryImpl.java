package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardAndCategoryResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.category.repository.CategoryRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.jbaacount.board.entity.QBoard.board;

@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    private final CategoryRepository categoryRepository;

    @Override
    public List<BoardResponseDto> findAllBoards()
    {
        return query
                .select(extractBoardsInfo())
                .from(board)
                .orderBy(board.orderIndex.asc())
                .fetch();
    }

    @Override
    public List<BoardAndCategoryResponse> findAllBoardAndCategory()
    {
        List<BoardAndCategoryResponse> boardList = query
                .select(extractBoardAndCategories())
                .from(board)
                .orderBy(board.orderIndex.asc())
                .fetch();

        for (BoardAndCategoryResponse board : boardList)
        {
            board.setCategories(categoryRepository.findAllCategories(board.getId()));
        }

        return boardList;
    }

    @Override
    public long countBoard()
    {
        return query
                .select(board.count())
                .from(board)
                .fetchOne();
    }

    private ConstructorExpression<BoardAndCategoryResponse> extractBoardAndCategories()
    {
        return Projections.constructor(BoardAndCategoryResponse.class,
                board.id,
                board.name,
                board.orderIndex);
    }

    private ConstructorExpression<BoardResponseDto> extractBoardsInfo()
    {
        return Projections.constructor(BoardResponseDto.class,
                board.id,
                board.name,
                board.orderIndex);
    }
}
