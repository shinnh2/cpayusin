package com.jbaacount.controller;

import com.jbaacount.payload.request.MemberMailDto;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MailController
{
    private final AuthenticationService authenticationService;
    private final MailService mailService;

    @GetMapping("/email")
    public ResponseEntity<String> sendVerificationCode(@RequestBody @Valid MemberMailDto mailDto)
    {
        String email = mailDto.getEmail();

        mailService.sendVerificationCode(email);

        return ResponseEntity.ok("인증코드가 발송되었습니다. 5분 내로 인증을 완료해주세요.");
    }


}
