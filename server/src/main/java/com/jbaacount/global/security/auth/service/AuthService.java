package com.jbaacount.global.security.auth.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.redis.RedisRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService
{
    private final RedisRepository redisRepository;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    public void login(String email)
    {
        String refreshToken = jwtService.generateRefreshToken(email);
        redisRepository.saveRefreshToken(refreshToken, email);
    }

    public void logout(String refreshToken)
    {
        jwtService.isValidToken(refreshToken);

        if(hasKey(refreshToken))
        {
            redisRepository.deleteRefreshToken(refreshToken);
            return;
        }

        throw new RuntimeException("invalid token");
    }

    public String reissue(String accessToken, String refreshToken)
    {
        if(hasKey(refreshToken))
        {
            log.info("===reissue===");
            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            Claims claims = jwtService.getClaims(accessToken.substring(7));
            String email = claims.getSubject();
            List roles = (List) claims.get("roles");

            return jwtService.generateAccessToken(email, roles);
        }

        throw new RuntimeException("token expired");
    }

    private Boolean hasKey(String refreshToken)
    {
        jwtService.isValidToken(refreshToken);

        Boolean hasKey = redisTemplate.hasKey(refreshToken);
        return hasKey;
    }
}