package com.jbaacount.repository;

import com.jbaacount.payload.response.CategoryResponseDto;

import java.util.List;

public interface CategoryRepositoryCustom
{
    List<CategoryResponseDto> findAllCategories(Long boardId);

    long countCategory(Long boardId);

}
