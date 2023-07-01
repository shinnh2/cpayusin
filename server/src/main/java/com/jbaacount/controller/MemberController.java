package com.jbaacount.controller;

import com.jbaacount.domain.Member;
import com.jbaacount.dto.request.member.MemberPathDto;
import com.jbaacount.dto.request.member.MemberPostDto;
import com.jbaacount.dto.response.MemberResponseDto;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
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

        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@RequestBody MemberPathDto pathDto,
                                       @PathVariable("member-id") long memberId)
    {
        Member member = mapper.patchToMember(pathDto);
        member.setId(memberId);

        MemberResponseDto response = mapper.responseToMember(memberService.updateMember(member));

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") long memberId)
    {
        Member member = memberService.findById(memberId);
        MemberResponseDto response = mapper.responseToMember(member);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") long memberId)
    {
        memberService.findById(memberId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
