package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService
{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member)
    {
        log.info("===createMember===");
        String email = member.getEmail();
        String nickname = member.getNickname();

        verifyExistEmail(email);
        log.info("email = {}", email);
        verifyExistNickname(nickname);
        log.info("nickname = {}", nickname);

        Member savedMember = memberRepository.save(member);
        savedMember.setPassword(passwordEncoder.encode(member.getPassword()));
        List<String> roles = authorityUtils.createRoles(email);
        savedMember.setRoles(roles);

        return savedMember;
    }

    public Member updateMember(Member member, Member currentMember)
    {
        isTheSameUser(member.getId(), currentMember.getId());

        log.info("===updateMember===");
        Member findMember = getUser(member.getId());
        log.info("findMember email = {}", member.getEmail());

        Optional.ofNullable(member.getNickname())
                .ifPresent(nickname -> findMember.updateNickname(nickname));
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.updatePassword(password));

        return findMember;
    }

    @Transactional(readOnly = true)
    public Member getUser(long id)
    {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));
    }

    public void deleteById(long id, Member currentMember)
    {
        Member member = getUser(id);
        isTheSameUser(id, currentMember.getId());
        memberRepository.deleteById(id);
        log.info("deleted Member nickname = {}", member.getNickname());
    }

    @Transactional(readOnly = true)
    public void verifyExistEmail(String email)
    {
        memberRepository.findByEmail(email)
                .ifPresent(e -> {throw new BusinessLogicException(ExceptionMessage.EMAIL_ALREADY_EXIST);});
    }

    @Transactional(readOnly = true)
    public void verifyExistNickname(String nickname)
    {
         memberRepository.findByNickname(nickname)
                 .ifPresent(e -> {throw new BusinessLogicException(ExceptionMessage.NICKNAME_ALREADY_EXIST);});
    }

    private void isTheSameUser(Long memberId, Long loggedInMemberId)
    {
        if(memberId != loggedInMemberId)
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }

}
