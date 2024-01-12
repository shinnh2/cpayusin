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
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create/board")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<BoardResponse>> saveBoard(@RequestBody @Valid BoardCreateRequest request,
                                                                   @AuthenticationPrincipal Member curentMember)
    {
        log.info("name = {}", request.getName());

        var data = boardService.createBoard(request, curentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/board/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateBoard(@RequestBody @Valid List<BoardUpdateRequest> requests,
                                      @AuthenticationPrincipal Member currentMember)
    {
        log.info("bulk update 시작");
        boardService.bulkUpdateBoards(requests, currentMember);
        log.info("bulk update 종료");

        List<BoardAndCategoryResponse> response = boardService.getAllBoardAndCategory();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/create/category")
    public ResponseEntity<GlobalResponse<CategoryResponse>> createCategory(@RequestBody @Valid CategoryCreateRequest request,
                                                                           @RequestParam Long board,
                                                                           @AuthenticationPrincipal Member currentMember)
    {
        log.info("postDto is admin = {}", request.getIsAdminOnly());
        var data = categoryService.createCategory(request, board, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }
}
