package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.response.BoardInfoForResponse;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class BoardService
{
    private final BoardRepository boardRepository;
    private final AuthorizationService authorizationService;

    public Board createBoard(Board board, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        return boardRepository.save(board);
    }

    public Board updateBoard(Long boardId, BoardPatchDto request, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        Board board = getBoardById(boardId);

        Optional.ofNullable(request.getName())
                .ifPresent(name -> board.updateName(name));
        Optional.ofNullable(request.getIsAdminOnly())
                .ifPresent(authority -> board.changeBoardAuthority(authority));
        return board;
    }

    @Transactional(readOnly = true)
    public BoardInfoForResponse getBoardInfoWithAllPosts(Long boardId, Pageable pageable)
    {
        return boardRepository.getBoardWithAllPostsInfo(boardId, pageable);
    }

    @Transactional(readOnly = true)
    public Board getBoardById(Long boardId)
    {
        return boardRepository.findById(boardId)
                .orElseThrow();
    }

    public void deleteBoard(Long boardId, Member currentMember)
    {
        authorizationService.isAdmin(currentMember);

        boardRepository.deleteById(boardId);
    }

}