package com.jbaacount.category.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CategoryPatchDto
{
    @NotSpace
    private String name;

    private boolean isAdminOnly;
    private Long boardId;
}
