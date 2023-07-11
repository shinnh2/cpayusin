package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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


    @BeforeEach
    public void createMember()
    {
        Member member1 = Member.builder()
                .nickname("회원1")
                .email("aaaa@naver.com")
                .password("123123")
                .build();

        Member member2 = Member.builder()
                .nickname("회원2")
                .email("bbbb@naver.com")
                .password("123123")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @AfterEach
    public void deleteAll()
    {
        memberRepository.deleteAll();
        memberRepository.flush();
    }

    @Test
    public void SignUp_WithValidInfo()
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

    @Test
    public void SignUp_WithInvalidInfo()
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

    @Test
    public void SignUp_WithDuplicatedEmail()
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

    @Test
    public void SignUp_WithDuplicatedNickname()
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

    @Test
    public void updateTest_WithCorrectInfo()
    {
        Member member = memberService.getMemberById(1L);
        MemberPatchDto request = new MemberPatchDto();
        request.setNickname("홍길동");

        Member updatedMember = memberService.updateMember(1L, request, member);

        assertThat(updatedMember.getId()).isEqualTo(1L);
        assertThat(updatedMember.getEmail()).isEqualTo("aaaa@naver.com");
        assertThat(updatedMember.getNickname()).isEqualTo("홍길동");
    }

    @Test
    public void updateTest_WithDifferentMember()
    {
        Member member1 = memberService.getMemberById(1L);
        Member member2 = memberService.getMemberById(2L);

        MemberPatchDto request = new MemberPatchDto();
        request.setNickname("홍길동");

        assertThrows(BusinessLogicException.class, () -> memberService.updateMember(1L, request, member2));
    }

    @Test
    public void getTest()
    {
        Member member = memberService.getMemberById(1L);

        String rawPassword = "123123";

        assertThat(member.getNickname()).isEqualTo("회원1");
        assertTrue(passwordEncoder.matches(rawPassword, member.getPassword()));
        assertThat(member.getEmail()).isEqualTo("aaaa@naver.com");
        assertThat(member.getRoles()).contains("USER");
    }

    @Test
    public void deleteTest()
    {
        Member firstMember = memberService.getMemberById(1L);
        Member secondMember = memberService.getMemberById(2L);

        memberService.deleteById(firstMember.getId(), firstMember);

        Optional<Member> deletedMember = memberRepository.findById(1L);
        assertFalse(deletedMember.isPresent());
    }

    @Test
    public void deleteTest_WithDifferentMember()
    {
        Member firstMember = memberService.getMemberById(1L);
        Member secondMember = memberService.getMemberById(2L);

        assertThrows(BusinessLogicException.class, () -> memberService.deleteById(firstMember.getId(), secondMember));
    }
}