package com.jbaacount.board.service;

import com.jbaacount.board.dto.request.BoardPatchDto;
import com.jbaacount.board.dto.response.BoardAndCategoryResponse;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.dto.response.CategoryResponseDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.service.CategoryService;
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
    private CategoryService categoryService;

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

        for(int i = 1; i <= 3; i++)
        {
            Board board= Board.builder()
                    .name(i + "번째 게시판")
                    .isAdminOnly(false)
                    .build();

            boardService.createBoard(board, admin);

            Category category = Category.builder()
                            .name(i + "번째 카테고리")
                            .isAdminOnly(false)
                            .build();

            categoryService.createCategory(category, board.getId(), admin);
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

    @DisplayName("게시판 & 카테고리 수정 - 관리자")
    @Test
    void updateBoardAndCategory_Admin()
    {
        Member admin = getAdmin();
        List<BoardAndCategoryResponse> allBoardAndCategory = boardService.getAllBoardAndCategory();

        List<BoardPatchDto> boardRequests = new ArrayList<>();
        List<CategoryPatchDto> categoryRequests = new ArrayList<>();
        int num = 1;
        for(BoardAndCategoryResponse boardResponse : allBoardAndCategory)
        {
            List<CategoryResponseDto> categoryList = boardResponse.getCategories();
            for(CategoryResponseDto categoryResponse : categoryList)
            {
                CategoryPatchDto categoryPatchDto = new CategoryPatchDto(categoryResponse.getId(), "수정 후 카테고리" + num, true, categoryResponse.getOrderIndex(), null);
                categoryRequests.add(categoryPatchDto);

                BoardPatchDto boardPatchDto = new BoardPatchDto(boardResponse.getId(), "수정 후 게시판" + num, true, boardResponse.getOrderIndex(), null, categoryRequests);
                boardRequests.add(boardPatchDto);
                num++;
            }
        }

        boardService.bulkUpdateBoards(boardRequests, admin);

        List<BoardAndCategoryResponse> afterBulkUpdate = boardService.getAllBoardAndCategory();

        for(BoardAndCategoryResponse boardResponse : afterBulkUpdate)
        {
            List<CategoryResponseDto> categories = boardResponse.getCategories();

            String name = boardResponse.getName();
            assertThat(boardResponse.getName()).isEqualTo(name);
            System.out.println("board name = " + name);
            for(CategoryResponseDto category : categories)
            {
                String categoryName = category.getCategoryName();
                assertThat(category.getCategoryName()).isEqualTo(categoryName);
            }
        }
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