package com.jbaacount.post.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostPatchDto
{
    @NotSpace
    private String title;
    private String content;
    private Long categoryId;
}
