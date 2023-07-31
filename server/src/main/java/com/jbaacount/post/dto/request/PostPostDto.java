package com.jbaacount.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostPostDto
{
    private String title;
    private String content;
    private Long categoryId;
    private Long boardId;
}
