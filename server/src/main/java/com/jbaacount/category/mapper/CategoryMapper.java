package com.jbaacount.category.mapper;

import com.jbaacount.category.dto.request.CategoryPostDto;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryMapper
{
    private final PostMapper postMapper;

    public Category postToCategory(CategoryPostDto request)
    {
        if(request == null)
            return null;

        Category category = Category
                .builder()
                .name(request.getName())
                .isAdminOnly(request.getIsAdminOnly())
                .build();

        return category;
    }

    public CategoryResponseDto categoryToResponse(Category entity)
    {
        CategoryResponseDto response = new CategoryResponseDto();
        response.setId(entity.getId());
        response.setCategoryName(entity.getName());
        response.setAdminOnly(entity.getIsAdminOnly());
        response.setOrderIndex(entity.getOrderIndex());

        return response;
    }
}
