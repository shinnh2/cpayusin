package com.jbaacount.controller;

import com.jbaacount.global.security.dto.LoginDto;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.payload.request.MemberMailDto;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.AuthenticationResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AuthenticationController
{
    private final MemberMapper memberMapper;
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<AuthenticationResponse>> login(@RequestBody LoginDto loginDto)
    {
        String email = loginDto.getEmail();

        var data = authenticationService.login(email);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Refresh") String refreshToken)
    {
        authenticationService.logout(refreshToken);

        return new ResponseEntity<>("logout completed successfully", HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(@RequestHeader(value = "Authorization") String accessToken, @RequestHeader(value = "Refresh") String refreshToken)
    {
        String newAccessToken = authenticationService.reissue(accessToken, refreshToken);
        log.info("new access token = {}", newAccessToken);

        HttpHeaders response = authenticationService.setHeadersWithNewAccessToken(newAccessToken);

        return ResponseEntity.ok().headers(response).body("New accessToken has been issued successfully");
    }

    @PostMapping("/sign-up")
    public ResponseEntity enrollMember(@RequestBody @Valid MemberRegisterRequest request)
    {
        var data = authenticationService.register(request);

        log.info("===enrollMember===");
        log.info("user enrolled successfully");

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }


    @GetMapping("/verification-code")
    public ResponseEntity verifyCode(@RequestBody MemberMailDto mailDto)
    {
        boolean response = authenticationService.verifyCode(mailDto.getEmail(), mailDto.getVerificationCode());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody @Valid MemberMailDto mailDto)
    {
        MemberDetailResponse response = memberMapper.toMemberDetailResponse(authenticationService.resetPassword(mailDto.getEmail(), mailDto.getPassword()));

        return new ResponseEntity(response, HttpStatus.OK);
    }




}
