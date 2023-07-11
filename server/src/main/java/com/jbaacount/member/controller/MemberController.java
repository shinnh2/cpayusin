package com.jbaacount.member.controller;

import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController
{
    private final MemberService memberService;

    private final MemberMapper memberMapper;
    @PostMapping("/sign-up")
    public ResponseEntity enrollMember(@RequestBody @Valid MemberPostDto postDto)
    {
        Member signedUpMember = memberService.createMember(memberMapper.postToMember(postDto));

        MemberResponseDto response = memberMapper.memberToResponse(signedUpMember);

        log.info("===enrollMember===");
        log.info("user enrolled successfully");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@RequestBody @Valid MemberPatchDto patchDto,
                                       @PathVariable("member-id") @Positive long memberId,
                                       @AuthenticationPrincipal Member currentUser)
    {
        Member updatedMember = memberService.updateMember(memberId, patchDto, currentUser);
        MemberResponseDto response = memberMapper.memberToResponse(updatedMember);

        log.info("===updateMember===");
        log.info("user updated successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId)
    {
        Member member = memberService.getMemberById(memberId);
        MemberResponseDto response = memberMapper.memberToResponse(member);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       @AuthenticationPrincipal Member member)
    {
        log.info("===deleteMember===");
        log.info("user deleted successfully, deleted id = {}", memberId);
        memberService.deleteById(memberId, member);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}