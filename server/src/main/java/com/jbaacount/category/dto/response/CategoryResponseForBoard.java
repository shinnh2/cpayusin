package com.jbaacount.category.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CategoryResponseForBoard
{
    private Long id;
    private String name;

    @QueryProjection
    public CategoryResponseForBoard(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
