package com.jbaacount.payload.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class BoardSimpleResponse
{
    private Long id;
    private String name;

    @QueryProjection
    public BoardSimpleResponse(Long boardId, String name)
    {
        this.id = boardId;
        this.name = name;
    }
}
