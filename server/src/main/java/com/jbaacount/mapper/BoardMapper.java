package com.jbaacount.mapper;

import com.jbaacount.payload.request.BoardPostDto;
import com.jbaacount.payload.response.BoardResponseDto;
import com.jbaacount.model.Board;
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
                .orderIndex(entity.getOrderIndex())
                .build();

        return response;
    }
}
