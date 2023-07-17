package com.jbaacount.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberInfoForResponse
{
    private Long id;
    private String nickname;

    @QueryProjection
    public MemberInfoForResponse(Long id, String nickname)
    {
        this.id = id;
        this.nickname = nickname;
    }
}
