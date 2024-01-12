package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CategoryResponse
{
    private Long id;
    private String name;
    private boolean isAdminOnly;
    private Long orderIndex;

    @QueryProjection
    public CategoryResponse(Long id, String categoryName, boolean isAdminOnly, Long orderIndex)
    {
        this.id = id;
        this.name = categoryName;
        this.isAdminOnly = isAdminOnly;
        this.orderIndex = orderIndex;
    }
}
