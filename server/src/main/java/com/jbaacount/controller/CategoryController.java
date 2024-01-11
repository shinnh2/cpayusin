package com.jbaacount.controller;

import com.jbaacount.payload.request.CategoryPostDto;
import com.jbaacount.payload.response.CategoryResponseDto;
import com.jbaacount.model.Category;
import com.jbaacount.mapper.CategoryMapper;
import com.jbaacount.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.model.Member;
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


    @GetMapping("/board/{board-id}/category")
    public ResponseEntity getAllCategories(@PathVariable("board-id") @Positive Long boardId)
    {
        List<CategoryResponseDto> response = categoryService.getAllCategories(boardId);

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
