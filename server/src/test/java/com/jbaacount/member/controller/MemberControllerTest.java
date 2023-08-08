package com.jbaacount.member.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.google.gson.Gson;
import com.jbaacount.file.repository.FileRepository;
import com.jbaacount.file.service.FileService;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static com.jbaacount.utils.AppDocumentUtils.getRequestPreProcessor;
import static com.jbaacount.utils.AppDocumentUtils.getResponsePreProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class MemberControllerTest
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


    @Autowired
    private Gson gson;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void beforeEach() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        MockitoAnnotations.initMocks(this);

    }

    @DisplayName("회원가입 테스트")
    @Test
    void signup() throws Exception
    {
        String email = "mike@ticonsys.com";
        String password = "123456789";
        String nickname = "오랜만에 외출";

        MemberPostDto postDto = new MemberPostDto();
        postDto.setEmail(email);
        postDto.setPassword(password);
        postDto.setNickname(nickname);

        String content = gson.toJson(postDto);

        Member member = Member.builder()
                .nickname(postDto.getNickname())
                .email(postDto.getEmail())
                .password(postDto.getPassword())
                .build();

        member.setId(1L);
        member.setCreatedAt(LocalDateTime.now());
        member.setModifiedAt(LocalDateTime.now());
        member.setRoles(List.of("USER", "ADMIN"));


        MemberResponseDto response = new MemberResponseDto();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setEmail(member.getEmail());
        response.setProfileImage(null);
        response.setCreatedAt(member.getCreatedAt());
        response.setModifiedAt(member.getModifiedAt());


        given(memberMapper.postToMember(any(MemberPostDto.class))).willReturn(member);

        given(memberService.createMember(Mockito.any(Member.class))).willReturn(member);

        given(memberMapper.memberToResponse(Mockito.any(Member.class))).willReturn(response);


        ResultActions actions = mockMvc
                .perform(post("/members/sign-up")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));


        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nickname").value(postDto.getNickname()))
                .andExpect(jsonPath("$.data.email").value(postDto.getEmail()))
                .andDo(document("sign-up",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("nickname").description("회원 닉네임"),
                                        fieldWithPath("email").description("회원 이메일"),
                                        fieldWithPath("password").description("회원 비밀번호"))
                                ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.profileImage").type(JsonFieldType.STRING).optional().description("회원 프로필 사진"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원가입 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 일자")
                                )
                        )));
    }


    @DisplayName("회원 정보 수정")
    @WithMockUser
    @Test
    void updateMember() throws Exception
    {
        long memberId = 1L;

        String nickname = "홍길동";
        String password = "123456789!";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", "png", new byte[]{1,2,3,4});

        Member memberRequest = Member.builder()
                .nickname("nickname")
                .email("aaaa@naver.com")
                .password("123456789")
                .build();

        Member member = memberService.createMember(memberRequest);

        MemberPatchDto patchDto = new MemberPatchDto();
        patchDto.setNickname(nickname);
        patchDto.setPassword(password);

        String content = gson.toJson(patchDto);
        MockPart data = new MockPart("data", content.getBytes());

        data.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MemberResponseDto response = new MemberResponseDto();
        response.setId(memberId);
        response.setEmail("aaaa@naver.com");
        response.setNickname(nickname);
        response.setProfileImage("https://abcd.s3.ap-northeast-2.amazonaws.com/profile/efd8196a-3049-43b8-8664-452bba2b8bad.png");
        response.setCreatedAt(LocalDateTime.now());
        response.setModifiedAt(LocalDateTime.now());

        given(memberService.updateMember(anyLong(), any(MemberPatchDto.class), any(MockMultipartFile.class), any(Member.class))).willReturn(member);
        given(memberMapper.memberToResponse(member)).willReturn(response);


        ResultActions actions = mockMvc
                .perform(
                        multipartPatchBuilder("/members/{member-id}", memberId)
                                .file("image", mockMultipartFile.getBytes())
                                .part(data)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value(nickname))
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestParts(
                                partWithName("data").description("회원 정보").optional(),
                                partWithName("image").description("프로필 이미지").optional()

                        ),
                        requestPartFields("data",
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임").optional(),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호").optional()
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.profileImage").type(JsonFieldType.STRING).optional().description("회원 프로필 사진"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원가입 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description(" 정보 수정일자")
                                )
                        )
                ));
    }


    @DisplayName("회원 정보 조회 - 1명")
    @Test
    void getMember() throws Exception
    {
        Long memberId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();

        Member member = Member.builder()
                .email("mike@ticonsys.com")
                .password("123456789")
                .nickname("홍길동")
                .build();

        given(memberService.createMember(any(Member.class))).willReturn(member);

        Member createdMember = memberService.createMember(member);

        given(memberService.getMemberById(Mockito.anyLong())).willReturn(member);


        MemberResponseDto response = new MemberResponseDto();
        response.setId(memberId);
        response.setEmail(createdMember.getEmail());
        response.setNickname(createdMember.getNickname());
        response.setProfileImage(null);
        response.setCreatedAt(createdAt);
        response.setModifiedAt(modifiedAt);

        given(memberMapper.memberToResponse(createdMember)).willReturn(response);

        System.out.println("response id = " + response.getId());
        System.out.println("response nickname = " + response.getNickname());

        ResultActions actions =
                mockMvc.perform(get("/members/{member-id}", memberId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));


        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.email").value(response.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(response.getNickname()))
                .andDo(document("get-member",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                        fieldWithPath("data.profileImage").type(JsonFieldType.STRING).optional().description("회원 프로필 사진"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원가입 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description(" 정보 수정일자")
                        ))));
    }

    @DisplayName("회원 정보 조회 - 다수")
    @Test
    void getMembers()
    {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();

        Member member1 = Member.builder()
                .email("mike@ticonsys.com")
                .password("123456789")
                .nickname("홍길동")
                .build();

        Member member2 = Member.builder()
                .email("aaa@naver.com")
                .password("123456789")
                .nickname("홍길동")
                .build();


    }


    @DisplayName("회원 정보 삭제")
    @Test
    void deleteMember() throws Exception
    {
        Long memberId = 1L;
        Member member = Member.builder()
                .email("mike@ticonsys.com")
                .password("123456789")
                .nickname("홍길동")
                .build();

        given(memberService.getMemberById(Mockito.anyLong())).willReturn(member);

        ResultActions actions =
                mockMvc.perform(
                        delete("/members/{member-id}", memberId)
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자"))));
    }

    private MockMultipartHttpServletRequestBuilder multipartPatchBuilder(String url, Long memberId)
    {
        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.multipart("/members/{member-id}", memberId);

        builder.with(request -> {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        });

        return builder;
    }
}