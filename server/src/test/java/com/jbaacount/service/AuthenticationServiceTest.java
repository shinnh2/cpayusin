package com.jbaacount.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.dummy.DummyObject;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.repository.MemberRepository;
import com.jbaacount.repository.RedisRepository;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@Sql("classpath:db/teardown.sql")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest extends DummyObject
{
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private MemberService memberService;

    @Mock
    private RedisRepository redisRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomAuthorityUtils authorityUtils;

    @Spy // 진짜 객체를 InjectMocks 에 주입
    private ObjectMapper om;


    @Test
    void memberCreateTest() throws JsonProcessingException
    {
        // given
        String nickname = "test";
        String email = "aa@naver.com";
        String password = "12345";

        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setNickname(nickname);
        request.setEmail(email);
        request.setPassword(password);

        Member member = newMockMember(1L, email, nickname, "ADMIN");

        // stub 1
        Member convertedMember = MemberMapper.INSTANCE.toMemberEntity(request);
        assertThat(convertedMember.getNickname()).isEqualTo(nickname);
        assertThat(convertedMember.getPassword()).isEqualTo(password);

        // stub 2
        String encodedPassword = passwordEncoder.encode(password);
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();

        // stub 3
        when(memberService.save(any(Member.class))).thenReturn(member);


        // when
        MemberDetailResponse response = authenticationService.register(request);

        System.out.println("response = " + response.toString());
        // then
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(response.getScore()).isEqualTo(0);
        assertThat(response.getEmail()).isEqualTo(email);

    }

    @Test
    void verifyCode_test()
    {
        // given
        String email = "a.naver.com";
        String verificationCode = "ABCDEFGH";

        // stub 1
        when(redisRepository.getVerificationCodeByEmail(any(String.class))).thenReturn(verificationCode);

        // when
        String response = authenticationService.verifyCode(email, verificationCode);

        // then
        assertThat(response).isEqualTo("인증이 완료되었습니다.");
    }

    @Test
    void resetPassword_test()
    {
        // given
        Member member = newMockMember(1L, "aa@naver.com", "test", "ADMIN");
        String newPassword = "12345";

        // stub 1
        when(memberService.findMemberByEmail(any())).thenReturn(member);

        // stub 2
        member.updatePassword(passwordEncoder.encode(newPassword));

        // when
        MemberDetailResponse response = authenticationService.resetPassword(member.getEmail(), newPassword);

        // then
        assertThat(response.getEmail()).isEqualTo("aa@naver.com");
        assertThat(passwordEncoder.matches("12345", member.getPassword())).isTrue();
    }

    @Test
    void logout_test()
    {
        // given
        String email = "aa@naver.com";
        String refreshToken = jwtService.generateRefreshToken(email);
        System.out.println("refresh token = " + refreshToken);


        // stub 1
        when(jwtService.isValidToken(any())).thenReturn(true);

        // stub 2
        when(redisRepository.hasKey(any())).thenReturn(true);
        doNothing().when(redisRepository).deleteRefreshToken(refreshToken);

        // when
        String response = authenticationService.logout(refreshToken);

        // then
        assertThat(response).isEqualTo("로그아웃에 성공했습니다");
        System.out.println("response = " + response);
    }

    @Test
    void setHeader_test()
    {
        // given
        String email = "aa@naver.com";
        List<String> roles = List.of("ADMIN");
        String accessToken = "ASDFWEFSFDSFWEFSDFSDFSDF";

        // when
        HttpHeaders httpHeaders = authenticationService.setHeadersWithNewAccessToken(accessToken);
        System.out.println("accessToken = " + accessToken);

        // then
        assertThat(httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0)).isEqualTo("Bearer " + accessToken);
        System.out.println("authorization = " + httpHeaders.get("Authorization").get(0));
    }

}
