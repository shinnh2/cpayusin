package com.jbaacount.controller;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.BoardService;
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

    @PostMapping("/board/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<BoardResponse>> saveBoard(@Valid @RequestBody BoardCreateRequest request,
                                                                   @AuthenticationPrincipal Member currentMember)
    {
        log.info("role = {}", currentMember.getRoles());

        var data = boardService.createBoard(request, currentMember);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<List<BoardMenuResponse>>> updateBoard(@Valid @RequestBody List<BoardUpdateRequest> request,
                                                                               @AuthenticationPrincipal Member member)
    {
        log.info("update board = {}", request.toString());

        boardService.bulkUpdateBoards(request, member);
        var data = boardService.getMenuList();

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

}
