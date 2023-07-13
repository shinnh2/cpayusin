package com.jbaacount.post.dto.response;

import com.jbaacount.member.dto.response.MemberInfoResponseDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponseDto
{
    private Long id;
    private String title;
    private String content;
    private MemberInfoResponseDto member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}