package com.jbaacount.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostInfoForResponse
{
    private Long postId;
    private Long boardId;
    private String boardName;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String memberNickname;
    private LocalDateTime createdAt;

    @QueryProjection
    public PostInfoForResponse(Long postId, Long boardId, String boardName, Long categoryId, String categoryName, String title, String memberNickname, LocalDateTime createdAt)
    {
        this.postId = postId;
        this.boardId = boardId;
        this.boardName = boardName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.memberNickname = memberNickname;
        this.createdAt = createdAt;
    }
}
