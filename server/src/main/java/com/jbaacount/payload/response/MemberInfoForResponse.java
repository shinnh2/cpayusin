package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
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
