package com.jbaacount.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryPostDto
{
    private String name;
    private Boolean isAdminOnly;
    private Long boardId;
}
