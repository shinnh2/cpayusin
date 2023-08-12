package com.jbaacount.test;

import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.member.service.MemberService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestService implements CommandLineRunner
{
    private final MemberService memberService;


    @Override
    public void run(String... args) throws Exception
    {

        for(int i = 0; i < 20; i++)
        {
            memberService.createMember(Member.builder().email("aaaaa@naver.com"+i).nickname("회원테스트"+i).password("123456789").build());
        }
    }
}
