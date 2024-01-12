package com.jbaacount.controller;

import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.BoardService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class BoardController
{
    private final BoardService boardService;
    @GetMapping("/board/{board-id}")
    public ResponseEntity<GlobalResponse<BoardResponse>> getBoard(@PathVariable("board-id") @Positive Long boardId)
    {
        log.info("board id = {}", boardId);
        var data = boardService.getBoardResponse(boardId);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/board")
    public ResponseEntity<GlobalResponse<List<BoardResponse>>> getAllBoards()
    {
        var data = boardService.getAllBoards();

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/board-category")
    public ResponseEntity<GlobalResponse<List<BoardAndCategoryResponse>>> getAllBoardAndCategory()
    {
        var data = boardService.getAllBoardAndCategory();

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

}
