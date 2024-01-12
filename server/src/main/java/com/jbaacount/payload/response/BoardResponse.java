package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BoardResponse
{
    private Long id;
    private String name;
    private Long orderIndex;

    @QueryProjection
    public BoardResponse(Long id, String name, Long orderIndex)
    {
        this.id = id;
        this.name = name;
        this.orderIndex = orderIndex;
    }
}
