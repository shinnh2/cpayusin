package com.jbaacount.category.repository;

import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jbaacount.category.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public List<CategoryResponseDto> findAllCategories(Long boardId)
    {
        return query
                .select(extractCategories())
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(category.orderIndex.asc())
                .fetch();
    }

    private ConstructorExpression<CategoryResponseDto> extractCategories()
    {
        return Projections.constructor(CategoryResponseDto.class,
                category.id,
                category.name,
                category.isAdminOnly,
                category.orderIndex);
    }
}
