package com.jbaacount.category.dto.response;

import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class CategoryInfoForResponse
{
    private Long id;
    private String name;
    private PageDto<PostInfoForResponse> posts;

    @QueryProjection
    public CategoryInfoForResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
