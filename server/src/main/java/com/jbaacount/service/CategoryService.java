package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.model.Board;
import com.jbaacount.model.Category;
import com.jbaacount.model.Member;
import com.jbaacount.payload.response.CategoryResponseDto;
import com.jbaacount.repository.BoardRepository;
import com.jbaacount.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final AuthorizationService authorizationService;
    private final BoardRepository boardRepository;

    @Transactional
    public Category createCategory(Category category, Long boardId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        long orderIndex = categoryRepository.countCategory(boardId);
        category.updateOrderIndex(orderIndex + 1);

        category.addBoard(getBoard(boardId));
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(Long boardId)
    {
        return categoryRepository.findAllCategories(boardId);
    }

    private Board getBoard(Long id)
    {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.POST_NOT_FOUND));
    }
}
