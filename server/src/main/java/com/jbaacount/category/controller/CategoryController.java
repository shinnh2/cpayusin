package com.jbaacount.category.controller;

import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.dto.request.CategoryPostDto;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.mapper.CategoryMapper;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping
public class CategoryController
{
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/manage/category")
    public ResponseEntity createCategory(@RequestBody @Valid CategoryPostDto request,
                                         @AuthenticationPrincipal Member currentMember)
    {
        Category category = categoryMapper.postToCategory(request);
        Long boardId = request.getBoardId();

        log.info("postDto is admin = {}", request.getIsAdminOnly());
        Category savedCategory = categoryService.createCategory(category, boardId, currentMember);
        CategoryResponseDto response = categoryMapper.categoryToResponse(savedCategory);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/manage/{board-id}/category")
    public ResponseEntity updateCategory(@RequestBody @Valid List<CategoryPatchDto> requests,
                                         @PathVariable("board-id") @Positive long boardId,
                                         @AuthenticationPrincipal Member currentMember)
    {
        categoryService.categoryBulkUpdate(requests, boardId, currentMember);

        List<CategoryResponseDto> response = categoryService.getAllCategories(boardId);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/board/{board-id}/category")
    public ResponseEntity getAllCategories(@PathVariable("board-id") @Positive Long boardId)
    {
        List<CategoryResponseDto> response = categoryService.getAllCategories(boardId);

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
