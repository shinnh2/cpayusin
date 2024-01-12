package com.jbaacount.payload.response;

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

    private CategorySimpleResponse category;

    private MemberSimpleResponse member;

    private Long postId;
    private String title;
    private String content;
    private Integer commentsCount;

    private LocalDateTime createdAt;

    @QueryProjection
    public PostMultiResponse(BoardSimpleResponse board,
                             CategorySimpleResponse category,
                             MemberSimpleResponse member,
                             Long postId,
                             String title,
                             String content,
                             Integer commentsCount,
                             LocalDateTime createdAt)
    {
        this.board = board;
        this.category = category;
        this.member = member;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }
}
