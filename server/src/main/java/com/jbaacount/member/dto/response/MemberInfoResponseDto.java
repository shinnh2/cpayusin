package com.jbaacount.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberInfoResponseDto
{
    private Long id;

    private String nickname;

    private String email;

    private List<String> roles = new ArrayList<>();

}
