package com.jbaacount.category.service;

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

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    private final AuthorizationService authorizationService;

    public Category createCategory(Category category, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    public void deleteCategory(Long categoryId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        categoryRepository.deleteById(categoryId);
    }

}
