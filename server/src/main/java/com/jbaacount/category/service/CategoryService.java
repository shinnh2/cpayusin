package com.jbaacount.category.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;

    public Category createCategory(Category category, Long boardId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);
        Board board = getBoard(boardId);

        long orderIndex = categoryRepository.countCategory(boardId);
        category.updateOrderIndex(orderIndex + 1);

        category.addBoard(board);
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

    private Board getBoard(Long boardId)
    {
        return boardRepository.findById(boardId).orElseThrow();
    }
}
