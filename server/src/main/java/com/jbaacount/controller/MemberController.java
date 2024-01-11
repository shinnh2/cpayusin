package com.jbaacount.controller;

import com.jbaacount.global.dto.SingleResponseDto;
import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.payload.request.MemberPatchDto;
import com.jbaacount.payload.request.MemberPostDto;
import com.jbaacount.payload.response.MemberResponseDto;
import com.jbaacount.payload.response.MemberRewardResponse;
import com.jbaacount.model.Member;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.service.MemberService;
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

    @GetMapping("/verify/email")
    public ResponseEntity checkExistEmail(@RequestBody MemberPostDto memberPostDto)
    {
        return ResponseEntity.ok(memberService.checkExistEmail(memberPostDto.getEmail()));
    }

    @GetMapping("/verify/nickname")
    public ResponseEntity checkExistNickname(@RequestBody MemberPostDto memberPostDto)
    {
        return ResponseEntity.ok(memberService.checkExistNickname(memberPostDto.getNickname()));
    }
}
