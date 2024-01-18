package com.jbaacount.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class PostMultiResponse
{
    private BoardSimpleResponse board;
    private MemberSimpleResponse member;

    private Long categoryId;
    private String categoryName;
    private Long postId;
    private String title;
    private String content;
    private Integer commentsCount;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;
    private String timeInfo;

    @QueryProjection
    public PostMultiResponse(BoardSimpleResponse board,
                             Long categoryId,
                             String categoryName,
                             MemberSimpleResponse member,
                             Long postId,
                             String title,
                             String content,
                             Integer commentsCount,
                             LocalDateTime createdAt)
    {
        this.board = board;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.member = member;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }
}
