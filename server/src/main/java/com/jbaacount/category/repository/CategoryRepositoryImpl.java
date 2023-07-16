package com.jbaacount.category.repository;

import com.jbaacount.category.dto.response.CategoryInfoForResponse;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jbaacount.category.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom
{
    private final JPAQueryFactory query;

    @Override
    public Page<CategoryInfoForResponse> getAllCategoryInfo(Long boardId, Pageable pageable)
    {
        List<CategoryInfoForResponse> categoryResponse = query
                .select(getCategoryResponse())
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(category.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(categoryResponse, pageable, categoryResponse.size());
    }

    private ConstructorExpression<CategoryInfoForResponse> getCategoryResponse()
    {
        return Projections.constructor(CategoryInfoForResponse.class,
                category.id,
                category.name,
                category.board.id,
                category.board.name);
    }
}
