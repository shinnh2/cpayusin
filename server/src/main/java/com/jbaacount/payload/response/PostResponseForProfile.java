package com.jbaacount.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostResponseForProfile
{
    private Long id;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;
    private String timeInfo;

    @QueryProjection
    public PostResponseForProfile(Long id, String title, LocalDateTime createdAt)
    {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
