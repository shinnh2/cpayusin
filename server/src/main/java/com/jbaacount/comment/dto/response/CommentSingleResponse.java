package com.jbaacount.comment.dto.response;

import com.jbaacount.member.dto.response.MemberInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CommentSingleResponse
{
    private Long id;
    private Long parentId;
    private String text;
    private boolean voteStatus;
    private MemberInfoResponseDto member;
}
