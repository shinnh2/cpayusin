package com.jbaacount.category.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryPostDto
{
    @NotSpace
    @Max(value = 15, message = "카테고리 제목은 최대 15자까지 입력 가능합니다.")
    private String name;
    private Boolean isAdminOnly;
    private Long boardId;
}
