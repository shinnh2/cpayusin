package com.jbaacount.member.dto.response;

import com.jbaacount.member.entity.Member;
import lombok.*;

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
