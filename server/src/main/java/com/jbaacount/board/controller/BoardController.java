package com.jbaacount.board.controller;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.request.BoardPostDto;
import com.jbaacount.board.dto.response.BoardAndCategoryResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.mapper.BoardMapper;
import com.jbaacount.board.service.BoardService;
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

@Slf4j
@RequestMapping
@RequiredArgsConstructor
@RestController
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
        boardService.bulkUpdateBoards(requests, currentMember);

        List<BoardResponseDto> response = boardService.getAllBoards();

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
