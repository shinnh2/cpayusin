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
        memberService.deleteById(memberId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
