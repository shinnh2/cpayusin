package com.jbaacount.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentPostDto
{
    private Long parentId;
    private String text;
}
