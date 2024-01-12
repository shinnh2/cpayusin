package com.jbaacount.repository;

import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponse;

import java.util.List;

public interface BoardRepositoryCustom
{
    List<BoardResponse> findAllBoards();

    List<BoardAndCategoryResponse> findAllBoardAndCategory();

    long countBoard();
}
