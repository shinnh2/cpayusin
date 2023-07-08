package com.jbaacount.member.dto.request.member;

import lombok.Data;

@Data
public class MemberPostDto
{
    private String nickname;

    private String email;

    private String password;
}
