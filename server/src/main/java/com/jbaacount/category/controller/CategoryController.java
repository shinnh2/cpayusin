package com.jbaacount.category.controller;

import com.jbaacount.category.dto.CategoryPostDto;
import com.jbaacount.category.dto.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.mapper.CategoryMapper;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        Category savedCategory = categoryService.createCategory(category, currentMember);
        CategoryResponseDto response = categoryMapper.categoryToResponse(savedCategory);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity getCategory(@PathVariable("category-id") @Positive Long categoryId)
    {
        Category category = categoryService.getCategory(categoryId);
        CategoryResponseDto response = categoryMapper.categoryToResponse(category);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
