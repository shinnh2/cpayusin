package com.jbaacount.comment.dto.response;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class CommentSingleResponse
{
    private Long id;
    private Long parentId;
    private String text;
    private int voteCount;
    private boolean voteStatus;
    private LocalDateTime createdAt;
    private MemberInfoForResponse member;
}
