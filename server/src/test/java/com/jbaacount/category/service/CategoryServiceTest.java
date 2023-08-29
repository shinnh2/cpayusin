package com.jbaacount.category.service;

import com.jbaacount.board.entity.Board;
import com.jbaacount.board.repository.BoardRepository;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.category.dto.request.CategoryPatchDto;
import com.jbaacount.category.dto.response.CategoryResponseDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @DisplayName("카테고리 수정 - orderIndex 확인")
    @Test
    void updateCategory_orderIndexCheck()
    {
        //given
        Member admin = getAdmin();
        Board board1 = getBoard();
        Category category1 = categoryRepository.findByName(categoryName).get();
        Category category2 = categoryRepository.findByName("2번째 카테고리").get();
        Category category3 = categoryRepository.findByName("3번째 카테고리").get();
        Category category4 = categoryRepository.findByName("4번째 카테고리").get();
        Category category10 = categoryRepository.findByName("10번째 카테고리").get();


        Board board2 = Board.builder()
                .name("update")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board2, admin);

        List<CategoryPatchDto> requests = new ArrayList<>();

        CategoryPatchDto request1 = new CategoryPatchDto(category1.getId(), "java란", null, null, null, board2.getId());
        CategoryPatchDto request2 = new CategoryPatchDto(category2.getId(), null, false, 1L, null, null);
        CategoryPatchDto request3 = new CategoryPatchDto(category3.getId(), null, false, 1L, null, board2.getId());
        CategoryPatchDto request4 = new CategoryPatchDto(category4.getId(), null, false, 7L, null, null);

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        requests.add(request4);

        //when
        categoryService.categoryBulkUpdate(requests, board1.getId(), admin);

        //then
        category1 = categoryService.getCategory(category1.getId());
        em.refresh(category10);

        assertThat(category1.getName()).isEqualTo("java란");
        assertThat(category1.getBoard()).isEqualTo(board2);
        assertThat(category1.getOrderIndex()).isEqualTo(2L);
        assertThat(category2.getOrderIndex()).isEqualTo(1L);
        assertThat(category3.getOrderIndex()).isEqualTo(1L);
        assertThat(category4.getOrderIndex()).isEqualTo(7L);
        assertThat(category10.getOrderIndex()).isEqualTo(8L);

        List<CategoryResponseDto> responses = categoryService.getAllCategories(board1.getId());

        assertThat(responses.size()).isEqualTo(8);
    }

    @DisplayName("카테고리 수정 - 삭제 후 orderIndex 확인")
    @Test
    void updateCategory_delete()
    {
        //given
        Member admin = getAdmin();
        Board board1 = getBoard();
        Category category1 = categoryRepository.findByName(categoryName).get();
        Category category2 = categoryRepository.findByName("2번째 카테고리").get();
        Category category3 = categoryRepository.findByName("3번째 카테고리").get();
        Category category4 = categoryRepository.findByName("4번째 카테고리").get();
        Category category10 = categoryRepository.findByName("10번째 카테고리").get();


        Board board2 = Board.builder()
                .name("update")
                .isAdminOnly(true)
                .build();

        boardService.createBoard(board2, admin);

        List<CategoryPatchDto> requests = new ArrayList<>();

        CategoryPatchDto request1 = new CategoryPatchDto(category1.getId(), "java란", null, null, null, board2.getId());
        CategoryPatchDto request2 = new CategoryPatchDto(category2.getId(), null, false, null, true, null);
        CategoryPatchDto request3 = new CategoryPatchDto(category3.getId(), null, false, null, true, null);

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);

        //when
        categoryService.categoryBulkUpdate(requests, board1.getId(), admin);
        em.refresh(category4);
        em.refresh(category10);

        //then
        Long count = categoryRepository.findTheBiggestOrderIndex(board1.getId());
        assertThat(count).isEqualTo(7);
        assertThat(category4.getOrderIndex()).isEqualTo(1L);
        assertThat(category10.getOrderIndex()).isEqualTo(7L);

        Optional<Category> optionalCategory1 = categoryRepository.findByName("1번째 카테고리"); //삭제됨
        Optional<Category> optionalCategory2 = categoryRepository.findByName("2번째 카테고리"); //삭제됨
        Optional<Category> optionalCategory3 = categoryRepository.findByName("3번째 카테고리"); //삭제됨
        assertThat(optionalCategory1).isEmpty();
        assertThat(optionalCategory2).isEmpty();
        assertThat(optionalCategory3).isEmpty();
    }


    @DisplayName("카테고리 수정 - 유저")
    @Test
    void updateCategory_user()
    {
        Member user = getUser();
        Board board = getBoard();
        Category category1 = categoryRepository.findByName(categoryName).get();

        List<CategoryPatchDto> requests = new ArrayList<>();
        CategoryPatchDto request1 = new CategoryPatchDto(category1.getId(), "java란", null, null, null, null);
        requests.add(request1);

        assertThrows(BusinessLogicException.class, () -> categoryService.categoryBulkUpdate(requests, board.getId(), user));
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