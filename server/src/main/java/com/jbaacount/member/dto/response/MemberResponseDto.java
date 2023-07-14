package com.jbaacount.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class MemberResponseDto
{
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private List<String> roles = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @QueryProjection
    public MemberResponseDto(Long id, String nickname, String email, String password, LocalDateTime createdAt, LocalDateTime modifiedAt)
    {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
