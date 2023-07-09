package com.jbaacount.global.security.filter;

import com.jbaacount.global.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;

    private String ENDPOINT_WHITELIST[] = {
            "/member/login",
            "/member/sign-up",
            "/member/logout"
    };
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String accessToken = extractAccessToken(request);

        log.info("===doFilterInternal===");
        log.info("accessToken = {}", accessToken);


        if(jwtService.isValidToken(accessToken))
        {
            Authentication authentication = jwtService.getAuthentication(accessToken);
            log.info("header is not null");
            log.info("accessToken = {}", accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        return isSignUpRequest(request) || headerNotValidate(request);
    }


    private boolean isSignUpRequest(HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("===isSignUpRequest===");
        log.info("requestURI = {}", request);
        log.info("method = {}", method);

        return Arrays.stream(ENDPOINT_WHITELIST).anyMatch(a -> requestURI.equals(a) && method.equalsIgnoreCase("POST"));
    }


    private boolean headerNotValidate(HttpServletRequest request)
    {
        String header = request.getHeader(AUTHORIZATION);
        log.info("===headerNotValidate===");
        log.info("header = {}", header);

        return header == null || !header.startsWith("Bearer ");
    }

    private String extractAccessToken(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        log.info("===extractAccessToken===");
        log.info("header = {}", header);

        if(header != null && header.startsWith("Bearer "))
            return header.substring(7);

        return null;
    }

}
