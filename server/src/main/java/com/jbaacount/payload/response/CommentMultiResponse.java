package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentMultiResponse
{
    private Long id;
    private Long parentId;
    private String text;
    private Integer voteCount;
    private Boolean voteStatus;
    private Boolean isRemoved;

    private LocalDateTime createdAt;
    private MemberSimpleResponse member;
    private List<CommentMultiResponse> children = new ArrayList<>();

    @QueryProjection
    public CommentMultiResponse(Long id, Long parentId, String text, Integer voteCount, Boolean isRemoved, LocalDateTime createdAt, MemberSimpleResponse member)
    {
        this.id = id;
        this.parentId = parentId;
        this.text = text;
        this.voteCount = voteCount;
        this.isRemoved = isRemoved;
        this.createdAt = createdAt;
        this.member = member;
    }
}
