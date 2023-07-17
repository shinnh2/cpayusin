package com.jbaacount.board.repository;

import com.jbaacount.board.dto.response.BoardInfoForResponse;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom
{
    BoardInfoForResponse getBoardResponseInfo(Long boardId, Pageable pageable);
}
