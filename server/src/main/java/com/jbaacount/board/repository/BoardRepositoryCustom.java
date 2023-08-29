package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardAndCategoryResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;

import java.util.List;

public interface BoardRepositoryCustom
{
    List<BoardResponseDto> findAllBoards();

    List<BoardAndCategoryResponse> findAllBoardAndCategory();

    void bulkUpdateOrderIndex(Long start, Long end, int num);

    Long findTheBiggestOrderIndex();

    long countBoard();
}
