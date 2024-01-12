package com.jbaacount.controller;

import com.jbaacount.payload.response.CategoryResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping("{category-id}")
    public ResponseEntity<GlobalResponse<CategoryResponse>> getCategory(@PathVariable("category-id") Long categoryId)
    {
        var data = categoryService.getCategoryResponse(categoryId);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<CategoryResponse>>> getAllCategories(@RequestParam Long board)
    {
        var data = categoryService.getAllCategories(board);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }


}
