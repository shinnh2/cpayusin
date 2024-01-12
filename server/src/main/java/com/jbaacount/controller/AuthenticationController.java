package com.jbaacount.controller;

import com.jbaacount.global.security.dto.LoginDto;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.request.PasswordResetRequest;
import com.jbaacount.payload.response.AuthenticationResponse;
import com.jbaacount.payload.response.GlobalResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AuthenticationController
{
    private final AuthenticationService authenticationService;

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
        var data = authenticationService.logout(refreshToken);
        log.info("logout completed successfully");

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PostMapping("/reissue")
    public ResponseEntity<GlobalResponse<AuthenticationResponse>> reissue(@RequestHeader(value = "Authorization") String accessToken,
                                                                          @RequestHeader(value = "Refresh") String refreshToken)
    {
        var data = authenticationService.reissue(accessToken, refreshToken);

        authenticationService.setHeadersWithNewAccessToken(data.getAccessToken());

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<GlobalResponse<MemberDetailResponse>> enrollMember(@RequestBody @Valid MemberRegisterRequest request)
    {
        var data = authenticationService.register(request);

        log.info("===enrollMember===");
        log.info("user enrolled successfully");

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @GetMapping("/verification")
    public ResponseEntity<GlobalResponse<String>> verifyCode(@RequestParam String email,
                                                              @RequestParam String code)
    {
        var data = authenticationService.verifyCode(email, code);

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<GlobalResponse<MemberDetailResponse>> resetPassword(@RequestParam String email,
                                                                              @RequestBody @Valid PasswordResetRequest request)
    {
        var data = authenticationService.resetPassword(email, request.getPassword());

        return ResponseEntity.ok(new GlobalResponse<>(data));
    }
}
