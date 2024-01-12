package com.jbaacount.payload.response;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class MemberSimpleResponse
{
    private Long id;
    private String nickname;

    @QueryProjection
    public MemberSimpleResponse(Long memberId, String memberNickName)
    {
        this.id = memberId;
        this.nickname = memberNickName;
    }
}
