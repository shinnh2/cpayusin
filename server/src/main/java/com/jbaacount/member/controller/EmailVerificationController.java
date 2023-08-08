package com.jbaacount.member.controller;

import com.jbaacount.member.dto.request.MemberMailDto;
import com.jbaacount.member.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class EmailVerificationController
{
    private final MailService mailService;

    @GetMapping("/email")
    public ResponseEntity<String> sendVerificationCode(@RequestBody @Valid MemberMailDto mailDto)
    {
        String email = mailDto.getEmail();

        mailService.sendMailForSignUp(email);

        return ResponseEntity.ok("인증코드가 발송되었습니다. 5분 내로 인증을 완료해주세요.");
    }

    @GetMapping("/verification-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody @Valid MemberMailDto mailDto)
    {
        String email = mailDto.getEmail();
        String verificationCode = mailDto.getVerificationCode();
        boolean response = mailService.verifyCodeForSignUp(email, verificationCode);

        return ResponseEntity.ok(response);
    }
}
