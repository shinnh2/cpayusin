package com.jbaacount.global.security.filter;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.security.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
            "/member/sign-up"
    };
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String header = request.getHeader(AUTHORIZATION);
        log.info("===doFilterInternal===");
        log.info("header = {}", header);


        if(header != null)
        {
            try{
                String accessToken = header.replace("Bearer ", "");

                Authentication authentication = jwtService.getAuthentication(accessToken);
                log.info("header is not null");
                log.info("accessToken = {}", accessToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e){
                log.error("만료된 토큰입니다.");
            } catch (SignatureException se){
                request.setAttribute("exception", se);
            }
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        return isHeaderValid(request) || isSignUpRequest(request);
    }

    private boolean isSignUpRequest(HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        return Arrays.stream(ENDPOINT_WHITELIST).anyMatch(a -> requestURI.equals(a) && method.equalsIgnoreCase("POST"));
    }

    private boolean isHeaderValid(HttpServletRequest request)
    {
        String header = request.getHeader(AUTHORIZATION);

        return header != null && header.startsWith("Bearer ");
    }

}
