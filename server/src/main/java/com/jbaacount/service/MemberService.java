package com.jbaacount.service;

import com.jbaacount.domain.Member;
import com.jbaacount.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService
{
    private final MemberRepository memberRepository;

    public Member createMember(Member member)
    {
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    public Member updateMember(Member member)
    {
        Member findMember = findById(member.getId());

        Optional.ofNullable(member.getNickname())
                .ifPresent(nickname -> findMember.updateNickname(nickname));
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.updatePassword(password));

        return findMember;
    }

    @Transactional(readOnly = true)
    public Member findById(long id)
    {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void deleteById(long id)
    {
        Member member = findById(id);

        memberRepository.deleteById(id);
        log.info("deleted Member nickname = {}", member.getNickname());
    }

    @Transactional(readOnly = true)
    public Member verifyExistEmail(String email)
    {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public Member verifyExistNickname(String nickname)
    {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
