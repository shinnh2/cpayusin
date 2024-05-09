package com.jbaacount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.model.type.BoardType;
import com.jbaacount.payload.response.board.BoardChildrenResponse;
import com.jbaacount.payload.response.board.BoardMenuResponse;
import com.jbaacount.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.jbaacount.utils.DescriptionUtils.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser(roles = "ADMIN")
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(BoardController.class)
class BoardControllerTest
{
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp()
    {
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
    }

    @Test
    void getMenu() throws Exception
    {
        // given
        BoardMenuResponse response1 = BoardMenuResponse.builder()
                .id(1L)
                .name("board 1")
                .orderIndex(1)
                .isAdminOnly(true)
                .type(BoardType.BOARD.getCode())
                .build();

        BoardChildrenResponse childrenResponse1 =  BoardChildrenResponse.builder()
                .id(4L)
                .name("category 1")
                .orderIndex(1)
                .isAdminOnly(false)
                .type(BoardType.CATEGORY.getCode())
                .parentId(response1.getId())
                .build();

        BoardChildrenResponse childrenResponse2 =  BoardChildrenResponse.builder()
                .id(5L)
                .name("category 2")
                .orderIndex(2)
                .isAdminOnly(false)
                .type(BoardType.CATEGORY.getCode())
                .parentId(response1.getId())
                .build();

        response1.setCategory(List.of(childrenResponse1, childrenResponse2));


        BoardMenuResponse response2 = BoardMenuResponse.builder()
                .id(2L)
                .name("board 2")
                .orderIndex(2)
                .isAdminOnly(true)
                .type(BoardType.BOARD.getCode())
                .build();

        BoardMenuResponse response3 = BoardMenuResponse.builder()
                .id(3L)
                .name("board 3")
                .orderIndex(3)
                .isAdminOnly(false)
                .type(BoardType.BOARD.getCode())
                .build();

        List<BoardMenuResponse> responseList = List.of(response1, response2, response3);

        given(boardService.getMenuList()).willReturn(responseList);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/board/menu")
                        .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("get menu",
                        responseFields(
                                fieldWithPath("data[].id").description("게시판 고유 식별 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].name").description("게시판 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data[].isAdminOnly").description("관리자만 글을 쓸 수 있는지 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data[].orderIndex").description("게시판 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].isDeleted").description("삭제 여부").type(JsonFieldType.BOOLEAN).optional(),
                                fieldWithPath("data[].type").description("게시판 유형").type(JsonFieldType.STRING),

                                fieldWithPath("data[].category").description("하위 게시판 정보").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("data[].category[].id").description("하위 게시판 아이디").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].category[].name").description("하위 게시판 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data[].category[].orderIndex").description("하위 게시판 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].category[].type").description("게시판 유형").type(JsonFieldType.STRING),
                                fieldWithPath("data[].category[].parentId").description("상위 게시판의 고유 식별 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].category[].isAdminOnly").description("관리자만 글을 쓸 수 있는지 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data[].category[].isDeleted").description("삭제 여부").type(JsonFieldType.BOOLEAN).optional(),

                                fieldWithPath("pageInfo").type(JsonFieldType.NUMBER).description(PAGE_INFO).optional(),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description(SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description(CODE),
                                fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS)
                        )
                ));

        System.out.println("resultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }

    /*@Test
    void getBoardById() throws Exception
    {
        // given
        BoardResponse response = BoardResponse.builder()
                .id(3L)
                .name("운영체제의 종류")
                .orderIndex(1)
                .parentId(2L)
                .isAdminOnly(false)
                .build();

        given(boardService.findBoardById(any(Long.class))).willReturn(response);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/v1/board/single-info/" + response.getId().toString())
                        .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("get board detail",
                        pathParameters(
                                parameterWithName("board-id").description("게시판 고유 식별 번호")
                        ),

                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시판 고유 식별 번호"),
                                fieldWithPath("data.name").type(JsonFieldType.NUMBER).description("게시판 이름"),
                                fieldWithPath("data.orderIndex").type(JsonFieldType.NUMBER).description("게시판 순서"),
                                fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("상위 게시판 고유 식별 번호").optional(),
                                fieldWithPath("data.isAdminOnly").type(JsonFieldType.BOOLEAN).description("관리자만 글을 쓸 수 있는지 여부"),

                                fieldWithPath("pageInfo").type(JsonFieldType.NUMBER).description(PAGE_INFO).optional(),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description(SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description(CODE),
                                fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS)
                        )
                ));

        System.out.println("resultActions: " + resultActions.andReturn().getResponse().getContentAsString());
    }*/

    @Test
    void getCategoryList()  throws Exception
    {
        // given

        // when

        // then
    }
}