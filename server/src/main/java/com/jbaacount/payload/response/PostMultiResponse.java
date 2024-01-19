package com.jbaacount.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long memberId;
    private String memberName;

    private Long boardId;
    private String boardName;

    @JsonProperty("postId")
    private Long id;
    private String title;
    private String content;
    private Integer commentsCount;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;
    private String timeInfo;

    @QueryProjection
    public PostMultiResponse(Long memberId, String memberName, Long boardId, String boardName, Long id, String title, String content, Integer commentsCount, LocalDateTime createdAt)
    {
        this.memberId = memberId;
        this.memberName = memberName;
        this.boardId = boardId;
        this.boardName = boardName;

        this.id = id;
        this.title = title;
        this.content = content;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }
}
