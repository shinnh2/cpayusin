package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardAndCategoryResponse
{
    private Long id;
    private String name;
    private Long orderIndex;

    private List<CategoryResponse> categories = new ArrayList<>();

    @QueryProjection
    public BoardAndCategoryResponse(Long id, String name, Long orderIndex)
    {
        this.id = id;
        this.name = name;
        this.orderIndex = orderIndex;
    }
}
