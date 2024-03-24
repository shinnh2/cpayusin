package com.jbaacount.payload.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest
{
    private Long commentId;

    @NotSpace
    private String text;
}
