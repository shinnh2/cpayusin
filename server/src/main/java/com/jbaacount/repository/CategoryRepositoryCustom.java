package com.jbaacount.repository;

import com.jbaacount.payload.response.CategoryResponse;

import java.util.List;

public interface CategoryRepositoryCustom
{
    List<CategoryResponse> findAllCategories(Long boardId);

    long countCategory(Long boardId);

}
