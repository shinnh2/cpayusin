package com.jbaacount.payload.response.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreatedResponse
{
    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
