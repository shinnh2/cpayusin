package com.jbaacount.board.controller;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.request.BoardPostDto;
import com.jbaacount.board.dto.response.BoardInfoForResponse;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.dto.response.BoardResponseWithCategory;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.mapper.BoardMapper;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.dto.response.CategoryInfoForResponse;
import com.jbaacount.category.service.CategoryService;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/manage/board")
@RequiredArgsConstructor
@RestController
public class BoardController
{
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @PostMapping
    public ResponseEntity saveBoard(@RequestBody BoardPostDto request,
                                    @AuthenticationPrincipal Member curentMember)
    {
        log.info("name = {}", request.getName());

        Board board = boardMapper.boardPostToBoard(request);
        Board savedBoard = boardService.createBoard(board, curentMember);
        BoardResponseDto response = boardMapper.boardToResponse(savedBoard);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity updateBoard(@PathVariable("board-id") @Positive Long boardId,
                                      @RequestBody BoardPatchDto request,
                                      @AuthenticationPrincipal Member currentMember)
    {
        Board updatedBoard = boardService.updateBoard(boardId, request, currentMember);
        BoardResponseDto response = boardMapper.boardToResponse(updatedBoard);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/{board-id}")
    public ResponseEntity getBoardInfo(@PathVariable("board-id") @Positive Long boardId,
                                       Pageable pageable)
    {
        BoardInfoForResponse response = boardService.getBoardInfoWithAllPosts(boardId, pageable);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive Long boardId,
                                      @AuthenticationPrincipal Member currentMember)
    {
        boardService.deleteBoard(boardId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
