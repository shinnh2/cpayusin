package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.mapper.CategoryMapper;
import com.jbaacount.model.Board;
import com.jbaacount.model.Category;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.CategoryCreateRequest;
import com.jbaacount.payload.response.CategoryResponse;
import com.jbaacount.repository.BoardRepository;
import com.jbaacount.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final UtilService utilService;
    private final BoardRepository boardRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request, Long boardId, Member member)
    {
        Category category = CategoryMapper.INSTANCE.toCategoryEntity(request);
        utilService.isAdmin(member);
        category.addBoard(getBoard(boardId));

        long orderIndex = categoryRepository.countCategory(boardId);
        category.updateOrderIndex(orderIndex + 1);

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.INSTANCE.toCategoryResponse(savedCategory);
    }

    public Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    public CategoryResponse getCategoryResponse(Long categoryId)
    {
        Category category = getCategory(categoryId);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories(Long boardId)
    {
        return categoryRepository.findAllCategories(boardId);
    }

    private Board getBoard(Long id)
    {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }
}
