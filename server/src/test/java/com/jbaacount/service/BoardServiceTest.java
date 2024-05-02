package com.jbaacount.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.dummy.DummyObject;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.model.Board;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.board.BoardCreateRequest;
import com.jbaacount.payload.request.board.BoardUpdateRequest;
import com.jbaacount.payload.response.board.BoardMenuResponse;
import com.jbaacount.payload.response.board.BoardResponse;
import com.jbaacount.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Sql("classpath:db/teardown.sql")
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest extends DummyObject
{
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Spy
    private UtilService utilService;

    @Spy
    private ObjectMapper om;


    @DisplayName("게시판 생성")
    @Test
    void createBoard_Admin() throws JsonProcessingException
    {
        // given
        Long boardId = 1L;
        String name = "게시판";
        BoardCreateRequest request = new BoardCreateRequest();
        request.setName(name);
        request.setIsAdminOnly(true);

        // stub 1
        Member admin = newMockMember(1L, "aaa@naver.com", "admin", "ADMIN");
        Member user = newMockMember(1L, "aaa@naver.com", "admin", "USER");
        Board board = newMockBoard(boardId, name, 1);

        when(boardRepository.save(any())).thenReturn(board);
        utilService.isAdmin(admin);
        assertThrows(BusinessLogicException.class, () -> utilService.isAdmin(user));

        // when
        BoardResponse response = boardService.createBoard(request, admin);
        String responseBody = om.writeValueAsString(response);

        System.out.println("response body = " + responseBody);

        // then
        assertThat(response.getName()).isEqualTo("게시판");

    }

    @Test
    void updateBoard()
    {
        // given
        Member member = newMockMember(1L, "aaa@naver.com", "admin", "ADMIN");

        Board board1 = newMockBoard(1L, "게시판1", 1);
        Board board2 = newMockBoard(2L, "게시판2", 2);
        Board board3 = newMockBoard(3L, "게시판3", 3);

        BoardUpdateRequest request1 = new BoardUpdateRequest();
        request1.setId(1L);
        request1.setName("게시판1");
        request1.setIsDeleted(true);

        BoardUpdateRequest request2 = new BoardUpdateRequest();
        request2.setId(2L);
        request2.setName("게시판2");
        request2.setOrderIndex(1);
        request2.setIsDeleted(true);

        BoardUpdateRequest request3 = new BoardUpdateRequest();
        request3.setId(3L);
        request3.setName("게시판3");
        request2.setOrderIndex(2);
        request3.setIsDeleted(true);

        List<BoardUpdateRequest> requests = List.of(request1, request2, request3);

        // stub 1
        when(boardRepository.findById(any())).thenReturn(Optional.of(board1));
        when(boardRepository.findById(any())).thenReturn(Optional.of(board2));
        when(boardRepository.findById(any())).thenReturn(Optional.of(board3));


        // when

        List<BoardMenuResponse> menuList = boardService.bulkUpdateBoards(requests, member);


    }

}

