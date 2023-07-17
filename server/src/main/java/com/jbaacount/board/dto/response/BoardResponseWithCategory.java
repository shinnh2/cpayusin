package com.jbaacount.board.dto.response;

import com.jbaacount.category.dto.response.CategoryResponseDto;
import lombok.Data;

@Data
public class BoardResponseWithCategory
{
    private Long id;
    private String name;
    private CategoryResponseDto category;
}
