package com.jbaacount.controller;

import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mail")
public class MailController
{
    private final MailService mailService;

    @GetMapping
    public ResponseEntity<GlobalResponse<String>> sendVerificationCode(@RequestParam String email)
    {
        mailService.sendVerificationCode(email);

        String data = "인증코드가 발송되었습니다. 5분 내로 인증을 완료해주세요.";

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

}
