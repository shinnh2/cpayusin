package com.jbaacount.board.dto.response;

import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BoardResponseWithCategory
{
    private Long id;
    private String name;
    private List<CategoryResponseDto> category;

    @QueryProjection
    public BoardResponseWithCategory(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
