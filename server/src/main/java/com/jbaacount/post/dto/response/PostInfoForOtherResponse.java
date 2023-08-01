package com.jbaacount.post.dto.response;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostInfoForOtherResponse
{
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private MemberInfoForResponse member;
    @QueryProjection
    public PostInfoForOtherResponse(Long id, String title, LocalDateTime createdAt, MemberInfoForResponse member)
    {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.member = member;
    }
}
