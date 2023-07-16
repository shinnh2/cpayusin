package com.jbaacount.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostPatchDto
{
    private Long categoryId;
    private String title;
    private String content;
}
