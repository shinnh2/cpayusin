package com.jbaacount.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberResponseDto
{
    private Long id;

    private String nickname;

    private String email;


    private List<String> roles = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    @QueryProjection
    public MemberResponseDto(Long id, String nickname, String email, LocalDateTime createdAt, LocalDateTime modifiedAt)
    {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
