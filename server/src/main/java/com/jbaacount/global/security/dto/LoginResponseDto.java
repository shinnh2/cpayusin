package com.jbaacount.global.security.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LoginResponseDto
{
    private Long id;
    private String email;
    private String nickname;
    private List<String> roles = new ArrayList<>();
    private LocalDateTime signedInAt;

    @Builder
    public LoginResponseDto(Long id, String email, String nickname, List<String> roles, LocalDateTime signedInAt)
    {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.roles = roles;
        this.signedInAt = signedInAt;
    }
}
