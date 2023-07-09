package com.jbaacount.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.global.security.dto.LoginDto;
import com.jbaacount.global.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        log.info("===attemptAuthentication===");
        log.info("loginDto.getEmail = {}", loginDto.getEmail());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException, IOException
    {

        log.info("===successfulAuthentication===");
        log.info("authentication.getName = {}", authentication.getName());

        String accessToken = jwtService.generateAccessToken(authentication);

        log.info("accessToken = {}", accessToken);
        String refreshToken = jwtService.generateRefreshToken();

        //TODO store the refreshToken in redis


        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("RefreshToken", refreshToken);

        log.info("response.setHeader / refreshToken = {}", refreshToken);
        log.info("response.setHeader = {}", response.getHeader(AUTHORIZATION));
        log.info("response status = {}", response.getStatus());

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
    }

}
