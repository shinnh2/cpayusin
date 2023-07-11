package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.repository.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

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
    private LocalValidatorFactoryBean validator;

    @AfterEach
    void deleteAll()
    {
        memberRepository.deleteAll();
    }

    @Test
    void SignUpMemberWith_ValidInfo()
    {
        String email = "ands0927@naver.com";
        String password = "123123";
        String nickname = "가나다라";


        MemberPostDto post = new MemberPostDto(nickname, email, password);
        Member requestMember = memberMapper.postToMember(post);

        Member createdMember = memberService.createMember(requestMember);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MemberPostDto>> violations = validator.validate(post);

        assertThat(createdMember.getNickname()).isEqualTo(nickname);
        assertThat(createdMember.getEmail()).isEqualTo(email);
        assertThat(createdMember.getRoles()).contains("USER");
        assert(violations.isEmpty());
    }

    @Test
    void SignUpMemberWith_InvalidInfo()
    {
        String email = "ands0927";
        String password = "123123";
        String nickname = "가나다라";

        Member member1 = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        MemberPostDto post = new MemberPostDto(nickname, email, password);
        Member requestMember = memberMapper.postToMember(post);
        memberService.createMember(requestMember);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MemberPostDto>> violations = validator.validate(post);

        assert(!violations.isEmpty());
    }
}