package com.jbaacount.category.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.entity.Category;
import com.jbaacount.category.repository.CategoryRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import com.jbaacount.utils.TestUtil;
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

    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    private static final String boardName = "첫번째 게시판";
    private static final String categoryName = "JPA란";

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

        Category category = Category.builder()
                .name(categoryName)
                .isAdminOnly(true)
                .build();

        categoryService.createCategory(category, board.getId(), admin);
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


    @DisplayName("카테고리 수정 - 관리자")
    @Test
    void updateCategory_admin1()
    {
        Member admin = getAdmin();
        Board board = getBoard();
        Category category = categoryRepository.findByName(categoryName).get();

        Board board2 = Board.builder()
                .name("update")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board2, admin);

        CategoryPatchDto request = new CategoryPatchDto();
        request.setName("java란");
        request.setBoardId(board2.getId());

        Category updatedCategory = categoryService.updateCategory(category.getId(), request, admin);

        assertThat(updatedCategory.getName()).isEqualTo("java란");
        assertThat(updatedCategory.getBoard().getName()).isEqualTo("update");
    }

    @DisplayName("카테고리 수정 - 관리자 - 글 쓰기 권한 체크")
    @Test
    void updateCategory_admin_isAdminTest()
    {
        Member admin = getAdmin();
        Board board = getBoard();
        Category category = categoryRepository.findByName(categoryName).get();

        Board board2 = Board.builder()
                .name("update")
                .isAdminOnly(false)
                .build();

        boardService.createBoard(board2, admin);

        CategoryPatchDto request = new CategoryPatchDto();
        request.setName("java란");
        request.setBoardId(board2.getId());

        Category updatedCategory = categoryService.updateCategory(category.getId(), request, admin);

        assertThat(updatedCategory.getName()).isEqualTo("java란");
        assertThat(updatedCategory.getIsAdminOnly()).isEqualTo(board2.getIsAdminOnly());
        assertThat(updatedCategory.getBoard().getName()).isEqualTo(board2.getName());

    }

    @DisplayName("카테고리 수정 - 유저")
    @Test
    void updateCategory_user()
    {
        Member user = getUser();
        Category category = categoryRepository.findByName(categoryName).get();

        CategoryPatchDto request = new CategoryPatchDto();
        request.setName("java란");

        assertThrows(BusinessLogicException.class, () -> categoryService.updateCategory(category.getId(), request, user));
    }

    @DisplayName("카테고리 삭제 - 관리자")
    @Test
    void deleteCategory_admin()
    {
        Member admin = getAdmin();
        Category category = categoryRepository.findByName(categoryName).get();

        categoryService.deleteCategory(category.getId(), admin);

        assertThat(categoryRepository.findByName(categoryName)).isEmpty();
    }

    @DisplayName("카테고리 삭제 - 유저")
    @Test
    void deleteCategory_user()
    {
        Member user = getUser();
        Category category = categoryRepository.findByName(categoryName).get();

        assertThrows(BusinessLogicException.class, () -> categoryService.deleteCategory(category.getId(), user));
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