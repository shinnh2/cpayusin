package com.jbaacount.category.mapper;

import com.jbaacount.category.dto.CategoryPostDto;
import com.jbaacount.category.dto.CategoryResponseDto;
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
                .isAdminOnly(request.isAdminOnly())
                .build();

        return category;
    }

    public CategoryResponseDto categoryToResponse(Category entity)
    {

        CategoryResponseDto response = CategoryResponseDto.builder()
                .id(entity.getId())
                .categoryName(entity.getName())
                .isAdminOnly(entity.isAdminOnly())
                .posts(postMapper.postEntityToListResponse(entity.getPosts()))
                .build();

        return response;
    }
}
