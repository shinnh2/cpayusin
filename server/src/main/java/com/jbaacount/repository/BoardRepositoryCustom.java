package com.jbaacount.repository;

import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponseDto;

import java.util.List;

public interface BoardRepositoryCustom
{
    List<BoardResponseDto> findAllBoards();

    List<BoardAndCategoryResponse> findAllBoardAndCategory();

    long countBoard();
}
