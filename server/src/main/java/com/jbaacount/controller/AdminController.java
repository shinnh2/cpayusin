package com.jbaacount.controller;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.request.CategoryCreateRequest;
import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.CategoryResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.BoardService;
import com.jbaacount.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/admin/manage")
public class AdminController
{
    private final BoardService boardService;
    private final CategoryService categoryService;

    @PostMapping("/board/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<BoardResponse>> saveBoard(@RequestBody @Valid BoardCreateRequest request,
                                                                   @AuthenticationPrincipal Member currentMember)
    {
        log.info("role = {}", currentMember.getRoles());

        var data = boardService.createBoard(request, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/board/update")
    public ResponseEntity<GlobalResponse<List<BoardAndCategoryResponse>>> updateBoard(@RequestBody @Valid List<BoardUpdateRequest> requests,
                                                                                      @AuthenticationPrincipal Member currentMember)
    {
        log.info("role = {}", currentMember.getRoles());
        log.info("bulk update 시작");
        boardService.bulkUpdateBoards(requests, currentMember);
        log.info("bulk update 종료");

        var data = boardService.getAllBoardAndCategory();

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PostMapping("/category/create")
    public ResponseEntity<GlobalResponse<CategoryResponse>> createCategory(@RequestBody @Valid CategoryCreateRequest request,
                                                                           @RequestParam Long board,
                                                                           @AuthenticationPrincipal Member currentMember)
    {
        log.info("postDto is admin = {}", request.getIsAdminOnly());
        var data = categoryService.createCategory(request, board, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }
}
