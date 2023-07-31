package com.jbaacount.post.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostPostDto
{
    @NotSpace
    private String title;
    private String content;
    private Long categoryId;
    private Long boardId;
}
