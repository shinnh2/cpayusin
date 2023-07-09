package com.jbaacount.global.security.auth.controller;

import com.jbaacount.global.security.auth.service.AuthService;
import com.jbaacount.global.security.dto.LoginResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginResponseDto responseDto, String refreshToken)
    {
        String email = responseDto.getEmail();
        authService.login(email, refreshToken);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(String refreshToken)
    {
        authService.logout(refreshToken);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/reissue")
    public ResponseEntity reissue(@RequestHeader(value = "RefreshToken") String refreshToken,
                                  Authentication authentication)
    {
        String accessToken = authService.reissue(refreshToken, authentication);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).build();
    }
}
