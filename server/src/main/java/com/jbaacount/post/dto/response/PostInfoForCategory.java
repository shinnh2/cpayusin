package com.jbaacount.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostInfoForCategory
{
    private Long postId;
    private String categoryName;
    private String title;
    private String memberNickname;
    private LocalDateTime createdAt;

    @QueryProjection
    public PostInfoForCategory(Long postId, String categoryName, String title, String memberNickname, LocalDateTime createdAt)
    {
        this.postId = postId;
        this.categoryName = categoryName;
        this.title = title;
        this.memberNickname = memberNickname;
        this.createdAt = createdAt;
    }
}
