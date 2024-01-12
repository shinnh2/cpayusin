package com.jbaacount.repository;

import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.jbaacount.model.QBoard.board;


@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    private final CategoryRepository categoryRepository;

    @Override
    public List<BoardResponse> findAllBoards()
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

    private ConstructorExpression<BoardResponse> extractBoardsInfo()
    {
        return Projections.constructor(BoardResponse.class,
                board.id,
                board.name,
                board.orderIndex);
    }
}
