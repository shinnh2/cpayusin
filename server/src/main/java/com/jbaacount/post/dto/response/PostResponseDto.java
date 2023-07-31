package com.jbaacount.post.dto.response;

import com.jbaacount.member.dto.response.MemberInfoForResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer voteCount;
    private boolean voteStatus;
    private MemberInfoForResponse member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
