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
    private int voteCount;
    private boolean voteStatus;
    private LocalDateTime createdAt;
    private MemberSimpleResponse member;
    private List<CommentMultiResponse> children = new ArrayList<>();

    @QueryProjection
    public CommentMultiResponse(Long id, Long parentId, String text, int voteCount, LocalDateTime createdAt, MemberSimpleResponse member)
    {
        this.id = id;
        this.parentId = parentId;
        this.text = text;
        this.voteCount = voteCount;
        this.createdAt = createdAt;
        this.member = member;
    }
}
