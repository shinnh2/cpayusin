package com.jbaacount.controller;

import com.jbaacount.payload.request.BoardPatchDto;
import com.jbaacount.payload.request.BoardPostDto;
import com.jbaacount.payload.response.BoardAndCategoryResponse;
import com.jbaacount.payload.response.BoardResponseDto;
import com.jbaacount.model.Board;
import com.jbaacount.mapper.BoardMapper;
import com.jbaacount.service.BoardService;
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

@Slf4j
@RequestMapping
@RequiredArgsConstructor
@RestController("/api/v1")
public class BoardController
{
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @PostMapping("/manage/board")
    public ResponseEntity saveBoard(@RequestBody @Valid BoardPostDto request,
                                    @AuthenticationPrincipal Member curentMember)
    {
        log.info("name = {}", request.getName());

        Board board = boardMapper.boardPostToBoard(request);
        Board savedBoard = boardService.createBoard(board, curentMember);
        BoardResponseDto response = boardMapper.boardToResponse(savedBoard);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/manage/board")
    public ResponseEntity updateBoard(@RequestBody @Valid List<BoardPatchDto> requests,
                                      @AuthenticationPrincipal Member currentMember)
    {
        log.info("bulk update 시작");
        boardService.bulkUpdateBoards(requests, currentMember);
        log.info("bulk update 종료");

        List<BoardAndCategoryResponse> response = boardService.getAllBoardAndCategory();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/board/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id") @Positive Long boardId)
    {
        Board board = boardService.getBoardById(boardId);
        BoardResponseDto response = boardMapper.boardToResponse(board);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/board")
    public ResponseEntity getAllBoards()
    {
        List<BoardResponseDto> response = boardService.getAllBoards();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/board/all")
    public ResponseEntity getAllBoardAndCategory()
    {
        List<BoardAndCategoryResponse> response = boardService.getAllBoardAndCategory();

        return new ResponseEntity(response, HttpStatus.OK);
    }



}
