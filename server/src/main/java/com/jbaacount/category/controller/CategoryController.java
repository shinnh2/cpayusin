package com.jbaacount.category.controller;

import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.dto.request.CategoryPostDto;
import com.jbaacount.category.dto.response.CategoryInfoForResponse;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.mapper.CategoryMapper;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity createCategory(@RequestBody CategoryPostDto request,
                                         @AuthenticationPrincipal Member currentMember)
    {
        Category category = categoryMapper.postToCategory(request);
        Long boardId = request.getBoardId();

        log.info("postDto is admin = {}", request.getIsAdminOnly());
        Category savedCategory = categoryService.createCategory(category, boardId, currentMember);
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
    public ResponseEntity getCategoryInfo(@PathVariable("category-id") @Positive Long categoryId, Pageable pageable)
    {
        CategoryInfoForResponse response = categoryService.getCategoryResponseInfo(categoryId, pageable);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/manage/category/{category-id}")
    public ResponseEntity deleteCategory(@PathVariable("category-id") @Positive Long categoryId,
                                         @AuthenticationPrincipal Member currentMember)
    {
        categoryService.deleteCategory(categoryId, currentMember);

        return ResponseEntity.noContent().build();
    }
}
