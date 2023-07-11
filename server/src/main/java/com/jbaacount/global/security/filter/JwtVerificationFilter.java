package com.jbaacount.global.security.filter;

import com.jbaacount.global.exception.InvalidTokenException;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.userdetails.MemberDetailsService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberDetailsService memberDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        log.info("===doFilterInternal===");

        try {
            String accessToken = resolveAccessToken(request);
            jwtService.isValidToken(accessToken);
            setAuthenticationToContext(jwtService.getClaims(accessToken));

            log.info("accessToken = {}", accessToken);
        } catch (InvalidTokenException e){
            log.error("Error processing JWT: {}", e.getMessage());
            throw e;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        String authorization = request.getHeader(AUTHORIZATION);
        log.info("===shouldNotFilter===");
        log.info("authorization = {}", authorization);

        return authorization == null || !authorization.startsWith("Bearer ");
    }

    private void setAuthenticationToContext(Claims claims)
    {
        String email = claims.getSubject();
        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));

        log.info("===setAuthenticationToContext===");
        log.info("authorities = {}", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveAccessToken(HttpServletRequest request)
    {
        return request.getHeader(AUTHORIZATION).substring(7);
    }
}