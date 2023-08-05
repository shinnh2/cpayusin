package com.jbaacount.member.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.Data;

@Data
public class MemberMailDto
{
    private String email;
    private String verificationCode;

    @NotSpace
    private String password;
}
