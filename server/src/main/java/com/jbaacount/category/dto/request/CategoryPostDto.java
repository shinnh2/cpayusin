package com.jbaacount.category.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryPostDto
{
    @NotSpace
    private String name;
    private Boolean isAdminOnly;
    private Long boardId;
}
