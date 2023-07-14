package com.jbaacount.category.service;

import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
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


}
