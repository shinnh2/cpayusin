package com.jbaacount.category.repository;

import com.jbaacount.category.dto.response.CategoryInfoForResponse;
import com.jbaacount.global.dto.PageDto;
import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.jbaacount.post.repository.PostRepository;
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
    private final PostRepository postRepository;
    @Override
    public CategoryInfoForResponse getCategoryInfo(Long categoryId, Pageable pageable)
    {
        CategoryInfoForResponse categoryInfo = query
                .select(extractCategoryInfo())
                .from(category)
                .where(category.id.eq(categoryId))
                .fetchOne();

        Page<PostInfoForResponse> posts = postRepository.getAllPostsInfoForCategory(categoryId, pageable);

        PageDto postsToPageDto = new PageDto(posts);
        categoryInfo.setPosts(postsToPageDto);

        return categoryInfo;
    }

    @Override
    public Page<CategoryInfoForResponse> getAllCategoryInfo(Long boardId, Pageable pageable)
    {
        List<CategoryInfoForResponse> categories = query
                .select(extractCategoryInfo())
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(category.name.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        return new PageImpl<>(categories, pageable, categories.size());
    }

    private ConstructorExpression<CategoryInfoForResponse> extractCategoryInfo()
    {
        return Projections.constructor(CategoryInfoForResponse.class,
                category.id,
                category.name);
    }

}
