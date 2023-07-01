package com.jbaacount.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto
{
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String authority;
}
