package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostSingleResponseDto
{
    private Long id;
    private String title;
    private String content;
    private List<FileResponseDto> files = new ArrayList<>();
    private Integer voteCount;
    private boolean voteStatus;
    private MemberInfoForResponse member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
