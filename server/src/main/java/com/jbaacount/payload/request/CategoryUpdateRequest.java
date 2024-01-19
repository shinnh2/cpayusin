package com.jbaacount.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest
{
    private Long id;
    private String name;
    private Integer orderIndex;
    private Boolean isAdminOnly;
    private Boolean isDeleted;
}
