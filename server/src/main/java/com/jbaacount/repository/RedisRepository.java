package com.jbaacount.repository;

import com.jbaacount.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RedisRepository
{
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    public void saveRefreshToken(String refreshToken, String email)
    {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken, email, jwtService.getRefreshTokenExpirationMinutes(), TimeUnit.MINUTES);
    }

    public void deleteRefreshToken(String refreshToken)
    {
        redisTemplate.delete(refreshToken);
    }
}
