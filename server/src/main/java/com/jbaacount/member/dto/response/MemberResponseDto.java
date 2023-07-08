package com.jbaacount.member.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MemberResponseDto
{
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private List<String> roles = new ArrayList<>();

}
