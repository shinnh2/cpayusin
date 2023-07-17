package com.jbaacount.board.dto.response;

import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BoardInfoForResponse
{
    private Long id;
    private String name;
    private Page<PostInfoForResponse> posts;

    @QueryProjection
    public BoardInfoForResponse(Long id, String name, Page<PostInfoForResponse> posts)
    {
        this.id = id;
        this.name = name;
        this.posts = posts;
    }
}
