package com.jbaacount.board.mapper;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.request.BoardPostDto;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.entity.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper
{
    public Board boardPostToBoard(BoardPostDto request)
    {
        if(request == null)
            return null;

        Board board = Board.builder()
                .name(request.getName())
                .isAdminOnly(request.getIsAdminOnly())
                .build();

        return board;
    }


    public BoardResponseDto boardToResponse(Board entity)
    {
        BoardResponseDto response = BoardResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();

        return response;
    }
}
