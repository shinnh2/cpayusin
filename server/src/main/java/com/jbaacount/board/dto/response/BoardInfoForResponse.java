package com.jbaacount.board.dto.response;

import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class BoardInfoForResponse
{
    private Long id;
    private String name;
    private List<CategoryResponseDto> category;
    private PageDto<PostInfoForResponse> posts;

    @QueryProjection
    public BoardInfoForResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
