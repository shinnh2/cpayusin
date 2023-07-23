package com.jbaacount.category.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto
{
    private Long id;
    private String categoryName;
    private boolean isAdminOnly;

    @QueryProjection
    public CategoryResponseDto(Long id, String categoryName)
    {
        this.id = id;
        this.categoryName = categoryName;
    }
}
