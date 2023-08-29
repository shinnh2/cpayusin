package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.utils.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
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

    @PersistenceContext
    private EntityManager em;

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);
        TestUtil.createUserMember(memberService);

        for(int i = 1; i < 10; i++)
        {
            boardService.createBoard(Board.builder()
                    .name(i + "번째 게시판")
                    .isAdminOnly(false)
                    .build(), admin);
        }
    }

    @DisplayName("게시판 생성 - 관리자")
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

    @DisplayName("게시판 생성 - 유저")
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

    @DisplayName("게시판 수정 - orderIndex 확인")
    @Test
    void updateBoard_orderIndexCheck()
    {
        //given
        Member admin = memberRepository.findByEmail(adminEmail).get();
        Board board1 = boardRepository.findBoardByName("1번째 게시판").get();
        Board board2 = boardRepository.findBoardByName("2번째 게시판").get();
        List<BoardPatchDto> requests = new ArrayList<>();
        BoardPatchDto request1 = new BoardPatchDto(board1.getId(), "첫번째 게시판", null, 3L, null);
        BoardPatchDto request2 = new BoardPatchDto(board2.getId(), "두번째 게시판", null, 1L, null);
        requests.add(request1);
        requests.add(request2);

        //when
        boardService.bulkUpdateBoards(requests, admin);

        //then
        board1 = boardService.getBoardById(board1.getId());
        board2 = boardService.getBoardById(board2.getId());

        assertThat(board1.getOrderIndex()).isEqualTo(3L);
        assertThat(board1.getName()).isEqualTo("첫번째 게시판");

        assertThat(board2.getOrderIndex()).isEqualTo(1L);
        assertThat(board2.getName()).isEqualTo("두번째 게시판");
    }


    @DisplayName("게시판 삭제 후 orderIndex 정렬 확인")
    @Test
    void deleteBoard_orderIndexCheck()
    {
        //given
        Member admin = memberRepository.findByEmail(adminEmail).get();
        Board board1 = boardRepository.findBoardByName("1번째 게시판").get();


        List<BoardPatchDto> requests = new ArrayList<>();
        BoardPatchDto request1 = new BoardPatchDto(board1.getId(), null, null, null, true);
        requests.add(request1);

        //when
        boardService.bulkUpdateBoards(requests, admin);

        //then
        Board board2 = boardService.getBoardById(boardRepository.findBoardByName("2번째 게시판").get().getId());
        Board board3 = boardService.getBoardById(boardRepository.findBoardByName("3번째 게시판").get().getId());
        Board board4 = boardService.getBoardById(boardRepository.findBoardByName("4번째 게시판").get().getId());

        em.refresh(board2);
        em.refresh(board3);
        em.refresh(board4);

        System.out.println("board 2 orderIndex = " + board2.getOrderIndex());
        System.out.println("board 3 orderIndex = " + board3.getOrderIndex());
        System.out.println("board 4 orderIndex = " + board4.getOrderIndex());

        assertThat(board2.getOrderIndex()).isEqualTo(1L);
        assertThat(board3.getOrderIndex()).isEqualTo(2L);
        assertThat(board4.getOrderIndex()).isEqualTo(3L);

    }

    @DisplayName("게시판 수정 - 유저")
    @Test
    void updateBoard_WithUser()
    {
        Member user = memberRepository.findByEmail(userEmail).get();
        Board board1 = boardRepository.findBoardByName("1번째 게시판").get();
        Board board2 = boardRepository.findBoardByName("2번째 게시판").get();

        //when
        List<BoardPatchDto> requests = new ArrayList<>();
        BoardPatchDto request1 = new BoardPatchDto(board1.getId(), "첫번째 게시판", null, 3L, null);
        BoardPatchDto request2 = new BoardPatchDto(board2.getId(), "두번째 게시판", null, 1L, null);
        requests.add(request1);
        requests.add(request2);

        assertThrows(BusinessLogicException.class, () -> boardService.bulkUpdateBoards(requests, user));
    }
}