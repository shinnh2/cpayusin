package com.jbaacount.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryPostDto
{
    private String name;
    private boolean isAdminOnly;
}
