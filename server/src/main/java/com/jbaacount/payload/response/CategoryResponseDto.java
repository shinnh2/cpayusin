package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CategoryResponseDto
{
    private Long id;
    private String categoryName;
    private boolean isAdminOnly;
    private Long orderIndex;

    @QueryProjection
    public CategoryResponseDto(Long id, String categoryName, boolean isAdminOnly, Long orderIndex)
    {
        this.id = id;
        this.categoryName = categoryName;
        this.isAdminOnly = isAdminOnly;
        this.orderIndex = orderIndex;
    }
}
