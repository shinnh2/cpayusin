package com.jbaacount.category.dto.response;

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
}
