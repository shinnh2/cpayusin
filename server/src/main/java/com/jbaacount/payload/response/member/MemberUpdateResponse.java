package com.jbaacount.payload.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateResponse
{
    private Long id;

    private String nickname;

    private String email;

    private String role;

    private int score;
}
