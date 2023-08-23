package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardResponseDto;
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
    public void bulkUpdateOrderIndex(Long start, Long end, int num)
    {
        query
                .update(board)
                .where(board.orderIndex.goe(start).and(board.orderIndex.loe(end)))
                .set(board.orderIndex, board.orderIndex.add(num))
                .execute();

        log.info("updated records");
    }

    @Override
    public Long findTheBiggestOrderIndex()
    {
        return query
                .select(board.orderIndex.max())
                .from(board)
                .fetchOne();
    }

    private ConstructorExpression<BoardResponseDto> extractBoardsInfo()
    {
        return Projections.constructor(BoardResponseDto.class,
                board.id,
                board.name,
                board.orderIndex);
    }
}
