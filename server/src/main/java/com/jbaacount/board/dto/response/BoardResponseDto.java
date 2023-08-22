package com.jbaacount.board.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BoardResponseDto
{
    private Long id;
    private String name;
    private Long orderIndex;

    @QueryProjection
    public BoardResponseDto(Long id, String name, Long orderIndex)
    {
        this.id = id;
        this.name = name;
        this.orderIndex = orderIndex;
    }
}
