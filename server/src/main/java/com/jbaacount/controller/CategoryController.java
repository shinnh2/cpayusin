package com.jbaacount.controller;

import com.jbaacount.payload.response.CategoryResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping("/single-info")
    public ResponseEntity<GlobalResponse<CategoryResponse>> getCategory(@RequestParam(name = "board") Long boardId)
    {
        var data = categoryService.getCategoryResponse(boardId);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/multi-info")
    public ResponseEntity<GlobalResponse<List<CategoryResponse>>> getAllCategories(@RequestParam Long board)
    {
        var data = categoryService.getAllCategories(board);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }


}
