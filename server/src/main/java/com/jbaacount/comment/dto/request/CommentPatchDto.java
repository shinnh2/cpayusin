package com.jbaacount.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentPatchDto
{
    private String text;
}
