package com.jbaacount.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.MockSetup;
import com.jbaacount.global.security.userdetails.MemberDetails;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.member.MemberUpdateRequest;
import com.jbaacount.repository.MemberRepository;
import com.jbaacount.service.FileService;
import com.jbaacount.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest extends MockSetup
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    private ObjectMapper om;

    @Captor
    private ArgumentCaptor<MultipartFile> multipartFileCaptor;

    @Autowired
    private FileService fileService;

    private static final String FILE_PATH1= "src/test/resources/image/";
    private static final String FILE_NAME1 = "photo1.jpeg";

    @BeforeEach
    void setUp()
    {
        Member mockMember = newMockMember(1L, "test@gmail.com", "test", "ADMIN");
        memberRepository.save(mockMember);

    }
}