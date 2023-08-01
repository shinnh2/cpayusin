package com.jbaacount.board.controller;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.request.BoardPostDto;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.dto.response.BoardWithAllCategoriesResponse;
import com.jbaacount.board.dto.response.BoardWithAllPostsResponse;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.mapper.BoardMapper;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.global.dto.PageDto;
import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.member.entity.Member;
import jakarta.validation.Valid;
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

    @PatchMapping("/manage/board/{board-id}")
    public ResponseEntity updateBoard(@PathVariable("board-id") @Positive Long boardId,
                                      @RequestBody @Valid BoardPatchDto request,
                                      @AuthenticationPrincipal Member currentMember)
    {
        Board updatedBoard = boardService.updateBoard(boardId, request, currentMember);
        BoardResponseDto response = boardMapper.boardToResponse(updatedBoard);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/board/posts/{board-id}")
    public ResponseEntity getBoardWithAllPosts(@PathVariable("board-id") @Positive Long boardId,
                                       Pageable pageable)
    {
        BoardWithAllPostsResponse response = boardService.getBoardInfoWithAllPosts(boardId, pageable);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/board/categories/{board-id}")
    public ResponseEntity getBoardWithAllCategories(@PathVariable("board-id") @Positive Long boardId)
    {
        BoardWithAllCategoriesResponse response = boardService.getBoardInfoWithAllCategories(boardId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/board")
    public ResponseEntity getAllBoards(Pageable pageable)
    {
        Page<BoardWithAllCategoriesResponse> response = boardService.getAllBoards(pageable);

        return new ResponseEntity(new PageDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/manage/board/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive Long boardId,
                                      @AuthenticationPrincipal Member currentMember)
    {
        boardService.deleteBoard(boardId, currentMember);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
