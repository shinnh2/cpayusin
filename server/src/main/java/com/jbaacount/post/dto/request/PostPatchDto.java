package com.jbaacount.post.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostPatchDto
{
    @NotSpace
    @Max(value = 20, message = "게시글 제목은 최대 50자까지 입력 가능합니다.")
    private String title;
    private String content;
    private Long categoryId;
}
