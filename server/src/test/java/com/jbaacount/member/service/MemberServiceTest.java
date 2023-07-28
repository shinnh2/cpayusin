package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest
{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String user1Email = "aaaa@naver.com";
    private static String user2Email = "bbbb@naver.com";
    @BeforeEach
    void createMember()
    {
        Member member1 = Member.builder()
                .nickname("회원1")
                .email(user1Email)
                .password("123123")
                .build();

        Member member2 = Member.builder()
                .nickname("회원2")
                .email(user2Email)
                .password("123123")
                .build();

        memberService.createMember(member1);
        memberService.createMember(member2);
    }

    @DisplayName("회원 가입 - 유효한 정보")
    @Test
    void SignUp_validInfo()
    {
        String email = "ands0927@naver.com";
        String password = "123123";
        String nickname = "가나다라";
        MemberPostDto post = new MemberPostDto(nickname, email, password);
        Member requestMember = memberMapper.postToMember(post);

        Member createdMember = memberService.createMember(requestMember);

        assertThat(createdMember.getNickname()).isEqualTo(nickname);
        assertThat(createdMember.getEmail()).isEqualTo(email);
        assertThat(createdMember.getRoles()).contains("USER");
        assertTrue(passwordEncoder.matches(password, createdMember.getPassword()));
    }

    @DisplayName("회원가입 - 잘못된 정보")
    @Test
    void SignUp_invalidInfo()
    {
        String email = "ands0927@naver.com";
        String password = "123123";
        String nickname = "가나다라";
        String firstMemberEmail = "aaaa@naver.com";
        MemberPostDto post = new MemberPostDto(nickname, email, password);
        Member requestMember = memberMapper.postToMember(post);

        Member createdMember = memberService.createMember(requestMember);

        assertThat(createdMember.getNickname()).isNotEqualTo("가나다");
        assertThat(createdMember.getPassword()).isNotEqualTo(password);
        assertThat(createdMember.getEmail()).isNotEqualTo(firstMemberEmail);
    }

    @DisplayName("회원 가입 - 중복 이메일")
    @Test
    void SignUp_duplicatedEmail()
    {
        String email = "aaaa@naver.com";
        String password = "123123";
        String nickname = "가나다라";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        assertThrows(BusinessLogicException.class, () -> memberService.createMember(member));
    }

    @DisplayName("회원 가입 - 중복 닉네임")
    @Test
    void SignUp_duplicatedNickname()
    {
        String email = "aaaabb@naver.com";
        String password = "123123";
        String nickname = "회원2";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        assertThrows(BusinessLogicException.class, () -> memberService.createMember(member));
    }

    @DisplayName("회원 수정 - 해당 유저가 시도")
    @Test
    void updateTest_sameUser()
    {
        Member member = memberRepository.findByEmail("aaaa@naver.com").get();
        MemberPatchDto request = new MemberPatchDto();
        request.setNickname("홍길동");

        Member updatedMember = memberService.updateMember(member.getId(), request, member);

        assertThat(updatedMember.getEmail()).isEqualTo("aaaa@naver.com");
        assertThat(updatedMember.getNickname()).isEqualTo("홍길동");
    }

    @DisplayName("회원 수정 - 다른 유저가 수정 시도")
    @Test
    void updateTest_differentMember()
    {
        Member member1 = memberRepository.findByEmail(user1Email).get();
        Member member2 = memberRepository.findByEmail(user2Email).get();

        MemberPatchDto request = new MemberPatchDto();
        request.setNickname("홍길동");

        assertThrows(BusinessLogicException.class, () -> memberService.updateMember(member1.getId(), request, member2));
    }

    @DisplayName("회원 조회")
    @Test
    void getTest()
    {
        String rawPassword = "123123";

        Member member = memberRepository.findByEmail("aaaa@naver.com").get();

        assertThat(member.getNickname()).isEqualTo("회원1");
        assertTrue(passwordEncoder.matches(rawPassword, member.getPassword()));
        assertThat(member.getEmail()).isEqualTo("aaaa@naver.com");
        assertThat(member.getRoles()).contains("USER");
    }

    @DisplayName("회원 조회 - 해당 유저")
    @Test
    void deleteTest()
    {
        Member firstMember = memberRepository.findByEmail("aaaa@naver.com").get();
        Member secondMember = memberRepository.findByEmail("bbbb@naver.com").get();

        memberService.deleteById(firstMember.getId(), firstMember);

        Optional<Member> deletedMember = memberRepository.findById(firstMember.getId());
        assertFalse(deletedMember.isPresent());
    }

    @DisplayName("회원 조회 - 다른 유저가 시도")
    @Test
    void deleteTest_WithDifferentMember()
    {
        Member firstMember = memberRepository.findByEmail("aaaa@naver.com").get();
        Member secondMember = memberRepository.findByEmail("bbbb@naver.com").get();

        assertThrows(BusinessLogicException.class, () -> memberService.deleteById(firstMember.getId(), secondMember));
    }
}