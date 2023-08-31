package com.jbaacount.board.service;

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

}