package com.jbaacount.controller;

import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberMailDto;
import com.jbaacount.payload.request.MemberPostDto;
import com.jbaacount.payload.response.MemberResponseDto;
import com.jbaacount.service.AuthenticationService;
import com.jbaacount.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/help")
public class AuthenticationController
{
    private final MemberMapper memberMapper;
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity enrollMember(@RequestBody @Valid MemberPostDto postDto)
    {
        Member signedUpMember = memberService.createMember(memberMapper.postToMember(postDto));

        MemberResponseDto response = memberMapper.memberToResponse(signedUpMember);

        log.info("===enrollMember===");
        log.info("user enrolled successfully");
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
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
        MemberResponseDto response = memberMapper.memberToResponse(authenticationService.resetPassword(mailDto.getEmail(), mailDto.getPassword()));

        return new ResponseEntity(response, HttpStatus.OK);
    }


}
