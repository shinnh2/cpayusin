package com.jbaacount.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class MemberResponseDto
{
    private Long id;

    private String nickname;

    private String email;

    private String profileImage;

    private int score;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    @QueryProjection
    public MemberResponseDto(Long id, String nickname, String email, String profileImage, LocalDateTime createdAt, LocalDateTime modifiedAt)
    {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
