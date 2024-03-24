package com.jbaacount.payload.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CommentCreateRequest
{
    @NotSpace
    private String text;

    private Long postId;

    private Long parentCommentId;
}
