package com.jbaacount.member;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.MemberUpdateRequest;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberServiceTest
{
    @Autowired
    MemberService memberService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    PasswordEncoder passwordEncoder;


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
