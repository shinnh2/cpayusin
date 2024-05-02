package com.jbaacount.payload.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.N;

@Data
@AllArgsConstructor
@N
public class MemberCreateResponse
{
    private Long id;

    private String nickname;

    private String email;

    private String role;

    private int score;
}
