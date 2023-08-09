/*
package com.jbaacount.board.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.google.gson.Gson;
import com.jbaacount.board.dto.request.BoardPostDto;
import com.jbaacount.board.dto.response.BoardResponseDto;
import com.jbaacount.board.entity.Board;
import com.jbaacount.board.mapper.BoardMapper;
import com.jbaacount.board.service.BoardService;
import com.jbaacount.file.repository.FileRepository;
import com.jbaacount.file.service.FileService;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.jbaacount.utils.AppDocumentUtils.getRequestPreProcessor;
import static com.jbaacount.utils.AppDocumentUtils.getResponsePreProcessor;
import static com.jbaacount.utils.TestUtil.setFieldsForEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class BoardControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AmazonS3 amazonS3;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private FileService fileService;

    @MockBean
    private MemberService memberService;


    @MockBean
    private MemberMapper memberMapper;

    @MockBean
    private BoardMapper boardMapper;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private BoardService boardService;

    @Autowired
    private Gson gson;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private ValueOperations<String, String> valueOperations;

    @MockBean
    private JwtService jwtService;

    private static String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sInN1YiI6Im1pa2VAdGljb25zeXMuY29tIiwiaWF0IjoxNjkxNTQ0NTYwLCJleHAiOjE2OTE1NTE3NjB9.-iNbMiD9UEz10rzLJ9Cw1CKqLRWDpNUOBwqnEYi_1nU";

    @BeforeEach
    void beforeEach() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        MockitoAnnotations.initMocks(this);

    }


    @DisplayName("게시판 생성")
    @WithMockUser(roles = {"ADMIN", "USER"})
    @Test
    void saveBoard() throws Exception
    {
        BoardPostDto postDto = new BoardPostDto();
        postDto.setName("SpringBoot");
        postDto.setIsAdminOnly(true);

        String content = gson.toJson(postDto);

        Board board = Board.builder()
                .name("SpringBoot")
                .isAdminOnly(true)
                .build();

        setFieldsForEntity(board, "id", 1L);
        setFieldsForEntity(board, "createdAt", LocalDateTime.now());
        setFieldsForEntity(board, "modifiedAt", LocalDateTime.now());

        Member member = Member.builder()
                .nickname("홍길동")
                .email("aaan@naver.com")
                .password("123456789")
                .build();

        member.setId(1L);
        member.setCreatedAt(LocalDateTime.now());
        member.setModifiedAt(LocalDateTime.now());
        member.setRoles(List.of("USER", "ADMIN"));

        BoardResponseDto response = new BoardResponseDto();
        response.setId(1L);
        response.setName("SpringBoot");


        given(boardMapper.boardPostToBoard(postDto)).willReturn(board);
        Board board1 = boardMapper.boardPostToBoard(postDto);
        System.out.println("board1 = " + board1.getName());

        given(boardService.createBoard(board, member)).willReturn(board);
        Board savedBoard = boardService.createBoard(board, member);
        System.out.println("saved board = " + savedBoard.getName());

        given(boardMapper.boardToResponse(board)).willReturn(response);
        BoardResponseDto responseDto = boardMapper.boardToResponse(savedBoard);
        System.out.println("response dto = " + responseDto.getName());

        verify(boardService).createBoard(any(Board.class), any(Member.class));

        ResultActions actions = mockMvc.perform(post("/manage/board")
                .header("Authorization", "Bearer " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        System.out.println("token = " + jwtToken);
        System.out.println("content = " + content);

        String responseString = actions.andReturn().getResponse().getContentAsString();
        System.out.println("response = " + responseString);


        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(postDto.getName()))
                .andDo(document("create-board",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").description("게시판 제목"),
                                        fieldWithPath("isAdminOnly").description("게시글 글쓰기 권한")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시판 식별자"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("게시판 제목")
                                )
                        )
                        ));

    }

}
*/
