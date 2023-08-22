package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardResponseDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jbaacount.board.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom
{
    private final JPAQueryFactory query;
    @Override
    public List<BoardResponseDto> findAllBoards()
    {
        return query
                .select(extractBoardsInfo())
                .from(board)
                .orderBy(board.orderIndex.asc())
                .fetch();
    }

    private ConstructorExpression<BoardResponseDto> extractBoardsInfo()
    {
        return Projections.constructor(BoardResponseDto.class,
                board.id,
                board.name,
                board.orderIndex);
    }
}
