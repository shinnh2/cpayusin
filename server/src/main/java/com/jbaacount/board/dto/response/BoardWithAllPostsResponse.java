package com.jbaacount.board.dto.response;

import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForOtherResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BoardWithAllPostsResponse
{
    private Long id;
    private String name;
    private PageDto<PostInfoForOtherResponse> posts;

    @QueryProjection
    public BoardWithAllPostsResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
