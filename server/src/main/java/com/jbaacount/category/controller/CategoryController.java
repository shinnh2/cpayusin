package com.jbaacount.category.controller;

import com.jbaacount.category.dto.CategoryPatchDto;
import com.jbaacount.category.dto.CategoryPostDto;
import com.jbaacount.category.dto.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.mapper.CategoryMapper;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.post.dto.response.PostInfoForCategory;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping
public class CategoryController
{
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/manage/category")
    public ResponseEntity createCategory(@RequestBody CategoryPostDto postDto,
                                         @AuthenticationPrincipal Member currentMember)
    {
        Category category = categoryMapper.postToCategory(postDto);

        log.info("postDto is admin = {}", postDto.getIsAdminOnly());
        Category savedCategory = categoryService.createCategory(category, currentMember);
        CategoryResponseDto response = categoryMapper.categoryToResponse(savedCategory);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/manage/category/{category-id}")
    public ResponseEntity updateCategory(@PathVariable("category-id") @Positive Long categoryId,
                                         @RequestBody CategoryPatchDto request,
                                         @AuthenticationPrincipal Member currentMember)
    {
        Category category = categoryService.updateCategory(categoryId, request, currentMember);
        CategoryResponseDto response = categoryMapper.categoryToResponse(category);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }


    @GetMapping("/category/{category-id}")
    public ResponseEntity getCategory(@PathVariable("category-id") @Positive Long categoryId, Pageable pageable)
    {
        Page<PostInfoForCategory> category = categoryService.getCategoryInfo(categoryId, pageable);

        return new ResponseEntity(category, HttpStatus.OK);
    }

    @DeleteMapping("/manage/category/{category-id}")
    public ResponseEntity deleteCategory(@PathVariable("category-id") @Positive Long categoryId,
                                         @AuthenticationPrincipal Member currentMember)
    {
        categoryService.deleteCategory(categoryId, currentMember);

        return ResponseEntity.noContent().build();
    }
}
