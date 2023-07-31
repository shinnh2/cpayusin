package com.jbaacount.comment.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentPatchDto
{
    @NotSpace
    private String text;
}
