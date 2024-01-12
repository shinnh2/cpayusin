package com.jbaacount.payload.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class MemberMailDto
{
    @Email
    private String email;
    private String verificationCode;

    @NotSpace
    private String password;
}
