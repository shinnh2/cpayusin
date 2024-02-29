package com.jbaacount.board;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.BoardCreateRequest;
import com.jbaacount.payload.request.BoardUpdateRequest;
import com.jbaacount.payload.request.CategoryUpdateRequest;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.BoardChildrenResponse;
import com.jbaacount.payload.response.BoardMenuResponse;
import com.jbaacount.payload.response.BoardResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.BoardService;
import com.jbaacount.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class BoardServiceTest
{
    @Autowired
    private BoardService boardService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    MemberService memberService;


    @BeforeEach
    void beforeEach()
    {
        MemberRegisterRequest admin = new MemberRegisterRequest();
        admin.setEmail("mike@ticonsys.com");
        admin.setNickname("운영자");
        admin.setPassword("123123123");

        MemberRegisterRequest user = new MemberRegisterRequest();
        user.setEmail("user@naver.com");
        user.setNickname("유저");
        user.setPassword("123123123");

        String boardName = "게시판1";

        BoardCreateRequest request = new BoardCreateRequest();
        request.setName(boardName);
        request.setIsAdminOnly(true);

        authService.register(admin);
        authService.register(user);

        boardService.createBoard(request, memberService.findMemberByEmail("mike@ticonsys.com"));
    }


    @DisplayName("운영자로 게시판 생성")
    @Test
    void createBoard_Admin()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");

        String boardName = "게시판1";

        BoardCreateRequest request = new BoardCreateRequest();
        request.setName(boardName);
        request.setIsAdminOnly(true);

        BoardResponse response = boardService.createBoard(request, admin);

        assertThat(response.getName()).isEqualTo(boardName);
    }

    @DisplayName("일반 유저로 게시판 생성")
    @Test
    void createBoard_User()
    {
        Member user = memberService.findMemberByEmail("user@naver.com");

        String boardName = "게시판1";

        BoardCreateRequest request = new BoardCreateRequest();
        request.setName(boardName);
        request.setIsAdminOnly(true);

        assertThrows(BusinessLogicException.class, () -> {boardService.createBoard(request, user);});
    }

    @Test
    void updateBoard()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");

        List<BoardMenuResponse> menuList = boardService.getMenuList();
        BoardMenuResponse boardMenuResponse = menuList.get(0);

        BoardCreateRequest request2 = new BoardCreateRequest();
        request2.setName("게시판1 - 하위1");
        request2.setIsAdminOnly(true);
        request2.setParentId(boardMenuResponse.getId());

        BoardCreateRequest request3 = new BoardCreateRequest();
        request3.setName("게시판1 - 하위2");
        request3.setIsAdminOnly(true);
        request3.setParentId(boardMenuResponse.getId());

        BoardResponse response2 = boardService.createBoard(request2, admin);
        BoardResponse response3 = boardService.createBoard(request3, admin);

        System.out.println("response2 = " + response2.toString());
        System.out.println("response3 = " + response3.toString());

        //
        BoardUpdateRequest updateRequest1 = new BoardUpdateRequest();
        updateRequest1.setId(boardMenuResponse.getId());
        updateRequest1.setName("게시판1 수정");
        updateRequest1.setOrderIndex(1);


        CategoryUpdateRequest categoryUpdateRequest2 = new CategoryUpdateRequest();
        categoryUpdateRequest2.setId(response2.getId());
        categoryUpdateRequest2.setName("게시판1 - 하위1 수정");
        updateRequest1.setOrderIndex(1);

        CategoryUpdateRequest categoryUpdateRequest3 = new CategoryUpdateRequest();
        categoryUpdateRequest3.setId(response3.getId());
        categoryUpdateRequest3.setIsDeleted(true);

        List<CategoryUpdateRequest> categoryUpdateRequests = List.of(categoryUpdateRequest2, categoryUpdateRequest3);
        updateRequest1.setCategory(categoryUpdateRequests);

        List<BoardUpdateRequest> requests = List.of(updateRequest1);
        boardService.bulkUpdateBoards(requests, admin);

        List<BoardMenuResponse> menuListAfterUpdate = boardService.getMenuList();
        BoardMenuResponse parentBoard = menuListAfterUpdate.get(0);

        System.out.println("테스트 = {}" + menuListAfterUpdate.toString());

        List<BoardChildrenResponse> category = parentBoard.getCategory();

        assertThat(menuListAfterUpdate.size()).isEqualTo(1);
        assertThat(category.size()).isEqualTo(1);
        assertThat(category.get(0).getName()).isEqualTo("게시판1 - 하위1 수정");

    }


}

