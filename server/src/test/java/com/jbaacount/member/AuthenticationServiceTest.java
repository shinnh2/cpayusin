package com.jbaacount.member;

import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
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
public class AuthenticationServiceTest
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
    void memberCreateTest()
    {
        //given
        String email = "aaa@naver.com";
        String nickname = "테스트1";
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("aaa@naver.com");
        request.setNickname("테스트1");
        request.setPassword("123123123");

        MemberDetailResponse registeredMember = authService.register(request);
        Member memberByEmail = memberService.findMemberByEmail(email);

        assertThat(registeredMember.getEmail()).isEqualTo(email);
        assertThat(registeredMember.getNickname()).isEqualTo(nickname);
        assertThat(memberByEmail.getRoles()).contains("USER");
    }

    @Test
    void resetPasswordTest()
    {
        String email = "mike@ticonsys.com";
        String oldPassword = "123123123";
        String newPassword = "111222333";

        Member beforeChange = memberService.findMemberByEmail("mike@ticonsys.com");
        authService.resetPassword(email, newPassword);
        Member afterChange = memberService.findMemberByEmail("mike@ticonsys.com");

        assertThat(passwordEncoder.matches(oldPassword, beforeChange.getPassword()));
        assertThat(passwordEncoder.matches(newPassword, afterChange.getPassword()));
    }

}
