package com.jbaacount.category.dto.response;

import com.jbaacount.post.dto.response.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto
{
    private Long id;
    private String categoryName;
    private boolean isAdminOnly;
}
