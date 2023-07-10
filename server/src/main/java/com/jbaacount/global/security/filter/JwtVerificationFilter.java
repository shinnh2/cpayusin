package com.jbaacount.global.security.filter;

import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final CustomAuthorityUtils authorityUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        log.info("===doFilterInternal===");

        try {
            String accessToken = resolveAccessToken(request);
            jwtService.isValidToken(accessToken);
            setAuthenticationToContext(jwtService.getClaims(accessToken));

            log.info("accessToken = {}", accessToken);
        } catch (SignatureException se)
        {
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);;
        } catch (Exception e) {
            request.setAttribute("exception", e);
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
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));

        log.info("===setAuthenticationToContext===");
        log.info("authorities = {}", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveAccessToken(HttpServletRequest request)
    {
        return request.getHeader(AUTHORIZATION).substring(7);
    }
}