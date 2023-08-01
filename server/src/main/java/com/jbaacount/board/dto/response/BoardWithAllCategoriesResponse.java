package com.jbaacount.board.dto.response;

import com.jbaacount.category.dto.response.CategoryResponseForBoard;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class BoardWithAllCategoriesResponse
{
    private Long id;
    private String name;
    private List<CategoryResponseForBoard> category;

    @QueryProjection
    public BoardWithAllCategoriesResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
