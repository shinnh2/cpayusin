package com.jbaacount.member.controller;

import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.member.dto.request.MemberPatchDto;
import com.jbaacount.member.dto.request.MemberPostDto;
import com.jbaacount.member.dto.response.MemberResponseDto;
import com.jbaacount.member.dto.response.MemberRewardResponse;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.mapper.MemberMapper;
import com.jbaacount.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@RequestPart(value = "data", required = false) @Valid MemberPatchDto patchDto,
                                       @RequestPart(value = "image", required = false)MultipartFile multipartFile,
                                       @PathVariable("member-id")@Positive long memberId,
                                       @AuthenticationPrincipal Member currentUser)
    {
        Member updatedMember = memberService.updateMember(memberId, patchDto, multipartFile, currentUser);
        MemberResponseDto response = memberMapper.memberToResponse(updatedMember);

        log.info("===updateMember===");
        log.info("user updated successfully");
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }


    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId)
    {
        Member member = memberService.getMemberById(memberId);
        MemberResponseDto response = memberMapper.memberToResponse(member);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllMembers(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(required = false) Long member,
                                        @PageableDefault(size = 8)Pageable pageable)

    {
        log.info("===getAllMembers===");
        SliceDto<MemberResponseDto> response = memberService.getAllMembers(keyword, member, pageable);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/score")
    public ResponseEntity get3MembersByScore()
    {
        LocalDateTime now = LocalDateTime.now();
        log.info("date = {}", now.getYear() + " " + now.getMonthValue());
        List<MemberRewardResponse> response = memberService.findTop3MembersByScore(now);

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

    @GetMapping("/exist/email")
    public ResponseEntity checkExistEmail(@RequestBody MemberPostDto memberPostDto)
    {
        return ResponseEntity.ok(memberService.checkExistEmail(memberPostDto.getEmail()));
    }

    @GetMapping("/exist/nickname")
    public ResponseEntity checkExistNickname(@RequestBody MemberPostDto memberPostDto)
    {
        return ResponseEntity.ok(memberService.checkExistNickname(memberPostDto.getNickname()));
    }
}
