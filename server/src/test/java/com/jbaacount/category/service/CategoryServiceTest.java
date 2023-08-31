package com.jbaacount.category.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
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
class CategoryServiceTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager em;

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    private static final String boardName = "첫번째 게시판";
    private static final String categoryName = "1번째 카테고리";

    @BeforeEach
    void beforeEach()
    {
        Member admin = TestUtil.createAdminMember(memberService);
        Member user = TestUtil.createUserMember(memberService);

        Board board = Board.builder()
                .name(boardName)
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board, admin);

        for(int i = 1; i <= 10; i++)
        {
            categoryService.createCategory(Category.builder()
                    .name(i + "번째 카테고리")
                    .isAdminOnly(true)
                    .build(), board.getId(), admin);
        }
    }

    @DisplayName("카테고리 생성 - 관리자")
    @Test
    void createCategory_admin()
    {
        Member admin = getAdmin();
        Board board = getBoard();

        Category category = Category.builder()
                .name("Spring이란")
                .isAdminOnly(true)
                .build();

        Category savedCategory = categoryService.createCategory(category, board.getId(), admin);

        assertThat(savedCategory.getName()).isEqualTo("Spring이란");
    }

    @DisplayName("카테고리 생성 - 유저")
    @Test
    void createCategory_user()
    {
        Member user = getUser();
        Board board = getBoard();

        Category category = Category.builder()
                .name("Spring이란")
                .isAdminOnly(true)
                .build();

        assertThrows(BusinessLogicException.class, () -> categoryService.createCategory(category, board.getId(), user));
    }


    private Board getBoard()
    {
        Board board = boardRepository.findBoardByName(boardName).get();
        return board;
    }

    private Member getAdmin()
    {
        return memberRepository.findByEmail(adminEmail).get();
    }

    private Member getUser()
    {
        return memberRepository.findByEmail(userEmail).get();
    }
}