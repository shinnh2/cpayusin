package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class CommentSingleResponse
{
    private Long memberId;
    private String nickname;

    private Long commentId;
    private Long parentId;
    private String text;
    private int voteCount;
    private boolean voteStatus;
    private Boolean isRemoved;
    private LocalDateTime createdAt;
}
