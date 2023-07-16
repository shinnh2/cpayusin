package com.jbaacount.category.service;

import com.jbaacount.category.dto.CategoryPatchDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.dto.response.PostInfoForCategory;
import com.jbaacount.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    private final AuthorizationService authorizationService;
    private final PostService postService;

    public Category createCategory(Category category, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, CategoryPatchDto request, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Category category = getCategory(categoryId);

        Optional.ofNullable(request.getName())
                .ifPresent(name -> category.updateName(name));
        Optional.ofNullable(request.isAdminOnly())
                .ifPresent(isAdminOnly -> category.changeCategoryAuthority(isAdminOnly));

        return category;
    }

    @Transactional(readOnly = true)
    public Page<PostInfoForCategory> getCategoryInfo(Long categoryId, Pageable pageable)
    {
        getCategory(categoryId);

        return postService.getPostInfoForCategory(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId)
    {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BusinessLogicException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }


    public void deleteCategory(Long categoryId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        categoryRepository.deleteById(categoryId);
    }

}
