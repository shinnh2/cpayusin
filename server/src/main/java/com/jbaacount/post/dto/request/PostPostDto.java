package com.jbaacount.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostPostDto
{
    private String title;
    private String content;
    private Long categoryId;
}
