package com.jbaacount.payload.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentPostDto
{
    private Long parentId;

    @NotSpace
    private String text;
}
