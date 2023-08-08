package com.jbaacount.member.service;

import com.jbaacount.file.service.FileService;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.global.service.AuthorizationService;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileService fileService;

    public Member createMember(Member member)
    {
        log.info("===createMember===");
        String email = member.getEmail();
        String nickname = member.getNickname();

        verifyExistEmail(email);
        verifyExistNickname(nickname);

        log.info("email = {}", email);
        log.info("nickname = {}", nickname);

        Member savedMember = memberRepository.save(member);
        savedMember.setPassword(passwordEncoder.encode(member.getPassword()));
        List<String> roles = authorityUtils.createRoles(email);
        savedMember.setRoles(roles);

        return savedMember;
    }

    public Member updateMember(Long memberId, MemberPatchDto request, MultipartFile multipartFile, Member currentMember)
    {
        Member findMember = getMemberById(memberId);
        authorizationService.isTheSameUser(memberId, currentMember.getId());

        log.info("===updateMember===");
        log.info("findMember email = {}", findMember.getEmail());

        if(multipartFile != null && !multipartFile.isEmpty())
        {
            fileService.deleteProfilePhoto(findMember);
            fileService.storeProfileImage(multipartFile, findMember);
            log.info("profile image = {}", findMember.getFile().getUrl());
        }

        if(request != null)
        {
            Optional.ofNullable(request.getNickname())
                    .ifPresent(nickname -> findMember.updateNickname(nickname));
            Optional.ofNullable(request.getPassword())
                    .ifPresent(password -> findMember.updatePassword(passwordEncoder.encode(password)));
        }

        return findMember;
    }

    @Transactional(readOnly = true)
    public Member getMemberById(long id)
    {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Slice<MemberResponseDto> getAllMembers(String keyword, Long memberId, Pageable pageable)
    {
        return memberRepository.findAllMembers(keyword, memberId, pageable);
    }

    public void deleteById(long memberId, Member currentMember)
    {
        Member member = getMemberById(memberId);
        authorizationService.checkPermission(memberId, currentMember);

        log.info("deleted Member nickname = {}", member.getNickname());

        if(member.getFile() != null)
        {
            log.info("delete image = {}", member.getFile().getUrl());
            fileService.deleteProfilePhoto(member);
        }

        memberRepository.deleteById(memberId);
    }

    private void verifyExistEmail(String email)
    {
        memberRepository.findByEmail(email)
                .ifPresent(e -> {throw new BusinessLogicException(ExceptionMessage.EMAIL_ALREADY_EXIST);});
    }

    private void verifyExistNickname(String nickname)
    {
         memberRepository.findByNickname(nickname)
                 .ifPresent(e -> {throw new BusinessLogicException(ExceptionMessage.NICKNAME_ALREADY_EXIST);});
    }

    @Transactional(readOnly = true)
    public boolean checkExistEmail(String email)
    {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean checkExistNickname(String nickname)
    {
        return memberRepository.findByNickname(nickname).isPresent();
    }

}
