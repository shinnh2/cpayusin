package com.jbaacount.member.controller;

import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.dto.request.member.MemberPathDto;
import com.jbaacount.member.dto.request.member.MemberPostDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController
{
    private final MemberService memberService;

    private final MemberMapper mapper;
    @PostMapping("/sign-up")
    public ResponseEntity enrollMember(@RequestBody MemberPostDto postDto)
    {
        Member signedUpMember = memberService.createMember(mapper.postToMember(postDto));

        MemberResponseDto response = mapper.responseToMember(signedUpMember);

        log.info("===enrollMember===");
        log.info("user enrolled successfully");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@RequestBody MemberPathDto pathDto,
                                       @PathVariable("member-id") long memberId,
                                       @AuthenticationPrincipal Member currentUser)
    {
        Member member = mapper.patchToMember(pathDto);
        member.setId(memberId);

        MemberResponseDto response = mapper.responseToMember(memberService.updateMember(member, currentUser));

        log.info("===updateMember===");
        log.info("user updated successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") long memberId)
    {
        Member member = memberService.getUser(memberId);
        MemberResponseDto response = mapper.responseToMember(member);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") long memberId,
                                       @AuthenticationPrincipal Member member)
    {
        log.info("===deleteMember===");
        log.info("user deleted successfully, deleted id = {}", memberId);
        memberService.deleteById(memberId, member);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
