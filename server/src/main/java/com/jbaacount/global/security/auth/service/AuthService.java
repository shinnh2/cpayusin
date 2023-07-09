package com.jbaacount.global.security.auth.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import com.jbaacount.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService
{
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RedisRepository redisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    
    public void login(String refreshToken, String email)
    {
        redisRepository.saveRefreshToken(refreshToken, email);
    }
    
    public void logout(String refreshToken)
    {
        redisRepository.deleteRefreshToken(refreshToken);
    }
    
    public String reissue(String refreshToken, Authentication authentication)
    {
         if(!jwtService.isValidToken(refreshToken))
             throw new BusinessLogicException(ExceptionMessage.TOKEN_EXPIRED);

        ValueOperations<String, String > valueOperations = redisTemplate.opsForValue();
        
        if(redisTemplate.hasKey(refreshToken))
        {
            String email = valueOperations.get(refreshToken);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));

            return jwtService.generateAccessToken(authentication);
        }

        else
            throw new BusinessLogicException(ExceptionMessage.TOKEN_EXPIRED);
        
    }
}
