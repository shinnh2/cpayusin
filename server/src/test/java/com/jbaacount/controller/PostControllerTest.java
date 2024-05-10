package com.jbaacount.controller;

import com.jbaacount.payload.request.post.PostCreateRequest;
import com.jbaacount.payload.response.post.PostCreateResponse;
import com.jbaacount.service.PostService;
import com.jbaacount.setup.RestDocsSetup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;
import java.time.LocalDateTime;

import static com.jbaacount.utils.DescriptionUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest extends RestDocsSetup
{
    @MockBean
    private PostService postService;

    private static final String FILE_PATH1= "src/test/resources/image/";
    private static final String FILE_NAME1 = "photo1.jpeg";
    private static final String CONTENT = "로렘 입숨(lorem ipsum; 줄여서 립숨, lipsum)은 출판이나 그래픽 디자인 분야에서 폰트, 타이포그래피, 레이아웃 같은 그래픽 요소나 시각적 연출을 보여줄 때 사용하는 표준 채우기 텍스트로, 최종 결과물에 들어가는 실제적인 문장 내용이 채워지기 전에 시각 디자인 프로젝트 모형의 채움 글로도 이용된다. 이런 용도로 사용할 때 로렘 입숨을 그리킹(greeking)이라고도 부르며, 때로 로렘 입숨은 공간만 차지하는 무언가를 지칭하는 용어로도 사용된다.";

    @Test
    void savePost() throws Exception
    {
        // given
        Long boardId = 1L;
        Long postId = 1L;
        String title = "첫번째 게시판";

        PostCreateRequest request = PostCreateRequest.builder()
                .title(title)
                .boardId(boardId)
                .content(CONTENT)
                .build();

        PostCreateResponse response = PostCreateResponse.builder()
                .id(postId)
                .title(title)
                .content(CONTENT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        byte[] requestBody = objectMapper.writeValueAsBytes(request);

        String fullPath = FILE_PATH1 + FILE_NAME1;

        MockMultipartFile image =
                new MockMultipartFile("files", FILE_NAME1, "image/jpeg", new FileInputStream(fullPath));

        MockMultipartFile data = new MockMultipartFile("data", null, MediaType.APPLICATION_JSON_VALUE, requestBody);

        given(postService.createPost(any(), any(), any())).willReturn(response);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.multipart("/api/v1/post/create")
                        .file(data)
                        .file(image)
                        .with(csrf())
                        .with(user(memberDetails)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(document("create post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("data").description("Json 데이터"),
                                partWithName("files").description("파일").optional()
                        ),

                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 고유 식별 번호"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("게시글 생성 날짜"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("게시글 수정 날짜"),

                                fieldWithPath("pageInfo").type(JsonFieldType.NUMBER).description(PAGE_INFO).optional(),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description(SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING).description(MESSAGE),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description(CODE),
                                fieldWithPath("status").type(JsonFieldType.STRING).description(STATUS)
                        )
                ));

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void updatePost()
    {
    }

    @Test
    void getPost()
    {
    }

    @Test
    void getMyPosts()
    {
    }

    @Test
    void getAllByBoardId()
    {
    }

    @Test
    void deletePost()
    {
    }
}