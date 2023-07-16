package com.jbaacount.category.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CategoryInfoForResponse
{
    private Long id;
    private String name;
    private Long boardId;
    private String boardName;

    @QueryProjection
    public CategoryInfoForResponse(Long id, String name, Long boardId, String boardName)
    {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
        this.boardName = boardName;
    }
}
