package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.dto.request.MemberPatchDto;
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
    private final AuthorizationService authorizationService;

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

    public Member updateMember(Long memberId, MemberPatchDto request, Member currentMember)
    {
        authorizationService.checkPermission(memberId, currentMember);

        log.info("===updateMember===");
        Member findMember = getMemberById(memberId);
        log.info("findMember email = {}", findMember.getEmail());

        Optional.ofNullable(request.getNickname())
                .ifPresent(nickname -> findMember.updateNickname(nickname));
        Optional.ofNullable(request.getPassword())
                .ifPresent(password -> findMember.updatePassword(password));

        return findMember;
    }

    @Transactional(readOnly = true)
    public Member getMemberById(long id)
    {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));
    }

    public void deleteById(long memberId, Member currentMember)
    {
        Member member = getMemberById(memberId);
        authorizationService.checkPermission(memberId, currentMember);
        memberRepository.deleteById(memberId);
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
}
