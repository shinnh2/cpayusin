package com.jbaacount.global.security.auth.controller;

import com.jbaacount.global.security.auth.service.AuthService;
import com.jbaacount.global.security.dto.LoginDto;
import com.jbaacount.global.security.dto.LoginResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class AuthController
{
    private final AuthService authService;

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto)
    {
        String email = loginDto.getEmail();

        authService.login(email);

        return new ResponseEntity<>("login completed successfully", HttpStatus.OK);
    }*/

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Refresh") String refreshToken)
    {
        authService.logout(refreshToken);

        return new ResponseEntity<>("logout completed successfully", HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader(value = "Authorization") String accessToken, @RequestHeader(value = "Refresh") String refreshToken)
    {
        String newAccessToken = authService.reissue(accessToken, refreshToken);

        log.info("new access token = {}", newAccessToken);
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
