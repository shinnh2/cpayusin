package com.jbaacount.member.controller;

import com.jbaacount.member.dto.request.MemberMailDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/members/help")
public class MailController
{
    private final MailService mailService;
    private final MemberMapper memberMapper;

    @GetMapping("/password")
    public ResponseEntity verifyEmail(@RequestBody MemberMailDto mailDto)
    {
        String verificationCode = mailService.sendMail(mailDto.getEmail());

        return new ResponseEntity(verificationCode, HttpStatus.OK);
    }

    @GetMapping("/verification-code")
    public ResponseEntity verifyCode(@RequestBody MemberMailDto mailDto)
    {
        boolean response = mailService.verifyCode(mailDto.getEmail(), mailDto.getVerificationCode());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/reset")
    public ResponseEntity resetPassword(@RequestBody @Valid MemberMailDto mailDto)
    {
        MemberResponseDto response = memberMapper.memberToResponse(mailService.resetPassword(mailDto.getEmail(), mailDto.getPassword()));

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
