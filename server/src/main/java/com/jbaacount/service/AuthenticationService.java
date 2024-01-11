package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationService
{
    private final MemberService memberService;

    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public boolean verifyCode(String email, String inputCode)
    {
        String verificationCode = redisTemplate.opsForValue().get(email);

        if(verificationCode == null)
            throw new BusinessLogicException(ExceptionMessage.EXPIRED_VERIFICATION_CODE);

        if(verificationCode.equals(inputCode))
            return true;

        throw new BusinessLogicException(ExceptionMessage.INVALID_VERIFICATION_CODE);
    }

    @Transactional
    public Member resetPassword(String email, String password)
    {
        Member member = memberService.findMemberByEmail(email);

        member.updatePassword(passwordEncoder.encode(password.toString()));

        return member;
    }



}
