package com.jbaacount.mapper;

import com.jbaacount.model.Category;
import com.jbaacount.payload.request.CategoryCreateRequest;
import com.jbaacount.payload.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper
{
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category toCategoryEntity(CategoryCreateRequest request);

    CategoryResponse toCategoryResponse(Category entity);
}
