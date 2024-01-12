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
    private Boolean isRemoved;

    @QueryProjection
    public CategorySimpleResponse(Long categoryId, String categoryName, Boolean isRemoved)
    {
        this.id = categoryId;
        this.name = categoryName;
        this.isRemoved = isRemoved;
    }
}
