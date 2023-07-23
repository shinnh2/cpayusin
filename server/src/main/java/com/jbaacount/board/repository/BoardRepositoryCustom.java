package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardInfoForResponse;
import com.jbaacount.board.dto.response.BoardResponseWithCategory;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom
{
    BoardInfoForResponse getBoardWithAllPostsInfo(Long boardId, Pageable pageable);
}
