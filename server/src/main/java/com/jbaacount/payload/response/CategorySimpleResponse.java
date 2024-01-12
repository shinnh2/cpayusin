package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CategorySimpleResponse
{
    private Long id;
    private String name;

    @QueryProjection
    public CategorySimpleResponse(Long categoryId, String categoryName)
    {
        this.id = categoryId;
        this.name = categoryName;
    }
}
