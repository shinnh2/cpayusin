package com.jbaacount.service;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.MemberUpdateRequest;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:db/teardown.sql")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void beforeEach()
    {
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("mike@ticonsys.com");
        request.setNickname("운영자");
        request.setPassword("123123123");

        authService.register(request);
    }


    @Test
    void memberUpdateTest()
    {
        Member admin = memberService.findMemberByEmail("mike@ticonsys.com");

        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setNickname("운영자수정");
        request.setPassword("111111111");

        memberService.updateMember(request, null, admin);

        Member afterChange = memberService.findMemberByEmail("mike@ticonsys.com");
        assertThat(afterChange.getNickname()).isEqualTo("운영자수정");
        assertThat(passwordEncoder.matches("111111111", afterChange.getPassword()));

    }

    @Test
    void getOneMemberTest()
    {
        Member member = memberService.findMemberByEmail("mike@ticonsys.com");
        MemberDetailResponse memberDetailResponse = memberService.getMemberDetailResponse(member.getId());

        assertThat(memberDetailResponse.getIsAdmin()).isTrue();
    }
}
