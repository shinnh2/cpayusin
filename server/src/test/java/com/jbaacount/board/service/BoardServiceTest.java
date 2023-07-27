package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.utils.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BoardServiceTest
{
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);
        Member user = TestUtil.createUserMember(memberService);

        Board board = Board.builder()
                .name("첫번째 게시판")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board, admin);
    }

    @AfterEach
    void deleteAll()
    {
        memberRepository.deleteAll();
        memberRepository.flush();

        boardRepository.deleteAll();
        boardRepository.flush();
    }

    @DisplayName("관리자 권한으로 게시판 생성 테스트")
    @Test
    void createBoard_WithAdmin()
    {
        Member admin = memberRepository.findByEmail(adminEmail).get();

        Board board = Board.builder()
                .name("첫번째 게시판")
                .isAdminOnly(true)
                .build();

        Board savedBoard = boardService.createBoard(board, admin);

        assertThat(savedBoard.getName()).isEqualTo("첫번째 게시판");
        assertThat(savedBoard.getIsAdminOnly()).isEqualTo(true);
    }

    @DisplayName("유저 권한으로 게시판 생성 테스트")
    @Test
    void createBoard_WithUser()
    {
        Member user = memberRepository.findByEmail(userEmail).get();

        Board board = Board.builder()
                .name("첫번째 게시판")
                .isAdminOnly(true)
                .build();

        assertThrows(BusinessLogicException.class, () -> boardService.createBoard(board, user));
    }

    @DisplayName("관리자 권한으로 게시판 수정 테스트")
    @Test
    void updateBoard_WithAdmin()
    {
        Member admin = memberRepository.findByEmail(adminEmail).get();
        Board board = boardRepository.findBoardByName("첫번째 게시판").get();
        BoardPatchDto request = new BoardPatchDto("업데이트 게시판", false);

        Board updatedBoard = boardService.updateBoard(board.getId(), request, admin);

        assertThat(updatedBoard.getName()).isEqualTo("업데이트 게시판");
        assertThat(updatedBoard.getIsAdminOnly()).isEqualTo(false);
    }

    @DisplayName("유저 권한으로 게시판 수정 테스트")
    @Test
    void updateBoard_WithUser()
    {
        Member user = memberRepository.findByEmail(userEmail).get();
        Board board = boardRepository.findBoardByName("첫번째 게시판").get();
        BoardPatchDto request = new BoardPatchDto("업데이트 게시판", false);

        assertThrows(BusinessLogicException.class, () -> boardService.updateBoard(board.getId(), request, user));
    }

    @DisplayName("관리자 권한으로 게시판 삭제 테스트")
    @Test
    void deleteBoard_WithAdmin()
    {
        Member admin = memberRepository.findByEmail(adminEmail).get();
        Board board = boardRepository.findBoardByName("첫번째 게시판").get();

        boardService.deleteBoard(board.getId(), admin);

        assertThat(boardRepository.findBoardByName("첫번째 게시판")).isEmpty();
    }

    @DisplayName("유저 권한으로 게시판 삭제 테스트")
    @Test
    void deleteBoard_WithUser()
    {
        Member user = memberRepository.findByEmail(userEmail).get();
        Board board = boardRepository.findBoardByName("첫번째 게시판").get();

        assertThrows(BusinessLogicException.class, () -> boardService.deleteBoard(board.getId(), user));
    }
}