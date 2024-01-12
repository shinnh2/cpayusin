package com.jbaacount.controller;

import com.jbaacount.global.dto.SliceDto;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.EmailRequest;
import com.jbaacount.payload.request.MemberUpdateRequest;
import com.jbaacount.payload.request.NicknameRequest;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.payload.response.MemberRewardResponse;
import com.jbaacount.service.MemberService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/member")
@RestController
public class MemberController
{
    private final MemberService memberService;

    @PatchMapping("/update")
    public ResponseEntity<GlobalResponse<MemberDetailResponse>> updateMember(@RequestPart(value = "data", required = false) @Valid MemberUpdateRequest patchDto,
                                       @RequestPart(value = "image", required = false)MultipartFile multipartFile,
                                       @AuthenticationPrincipal Member currentUser)
    {
        var data = memberService.updateMember(currentUser.getId(), patchDto, multipartFile, currentUser);

        log.info("===updateMember===");
        log.info("user updated successfully");
        return ResponseEntity.ok(new GlobalResponse<>(data));
    }


    @GetMapping("/single-info")
    public ResponseEntity<GlobalResponse<MemberDetailResponse>> getMember(@AuthenticationPrincipal Member member)
    {
        var data = memberService.getMemberDetailResponse(member.getId());

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/multi-info")
    public ResponseEntity getMemberList(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(required = false) Long member,
                                        @PageableDefault(size = 8)Pageable pageable)

    {
        log.info("===getAllMembers===");
        SliceDto<MemberDetailResponse> response = memberService.getAllMembers(keyword, member, pageable);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/score")
    public ResponseEntity<GlobalResponse<List<MemberRewardResponse>>> get3MembersByScore()
    {
        LocalDateTime now = LocalDateTime.now();
        log.info("date = {}", now.getYear() + " " + now.getMonthValue());
        var data = memberService.findTop3MembersByScore(now);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponse<String>> deleteMember(@AuthenticationPrincipal Member member)
    {
        log.info("===deleteMember===");

        memberService.deleteById(member);

        log.info("user deleted successfully, deleted id = {}", member.getId());
        return ResponseEntity.ok(new GlobalResponse<>("유저가 삭제되었습니다."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<GlobalResponse<String>> checkExistEmail(@RequestBody @Valid EmailRequest request)
    {
        var data = memberService.checkExistEmail(request.getEmail());

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/verify-nickname")
    public ResponseEntity<GlobalResponse<String>> checkExistNickname(@RequestBody @Valid NicknameRequest request)
    {
        var data = memberService.checkExistNickname(request.getNickname());

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }
}
