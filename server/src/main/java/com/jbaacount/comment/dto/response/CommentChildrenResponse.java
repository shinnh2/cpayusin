package com.jbaacount.comment.dto.response;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CommentChildrenResponse
{
    private Long id;
    private String text;
    private MemberInfoForResponse member;

    @QueryProjection
    public CommentChildrenResponse(Long id, String text, MemberInfoForResponse member)
    {
        this.id = id;
        this.text = text;
        this.member = member;
    }
}
