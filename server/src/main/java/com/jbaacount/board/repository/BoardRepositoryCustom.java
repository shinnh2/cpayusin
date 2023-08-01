package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardWithAllCategoriesResponse;
import com.jbaacount.board.dto.response.BoardWithAllPostsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom
{
    BoardWithAllPostsResponse getBoardWithAllPostsInfo(Long boardId, Pageable pageable);

    BoardWithAllCategoriesResponse getBoardWithAllCatetoriesInfo(Long boardId);

    Page<BoardWithAllCategoriesResponse> getAllBoardsAndCategories(Pageable pageable);
}
