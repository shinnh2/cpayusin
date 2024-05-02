package com.jbaacount.payload.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseForProfile
{
    private Long id;
    private Long postId;
    private String text;
    private Integer voteCount;
    private Boolean isRemoved;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;

    @QueryProjection
    public CommentResponseForProfile(Long id, Long postId, String text, Integer voteCount, Boolean isRemoved, LocalDateTime createdAt)
    {
        this.id = id;
        this.postId = postId;
        this.text = text;
        this.voteCount = voteCount;
        this.isRemoved = isRemoved;
        this.createdAt = createdAt;
    }
}
