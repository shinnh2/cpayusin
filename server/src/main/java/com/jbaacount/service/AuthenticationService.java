package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.exception.InvalidTokenException;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.model.Member;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.AuthenticationResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.repository.RedisRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthenticationService
{
    private final MemberService memberService;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisRepository redisRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public MemberDetailResponse register(MemberRegisterRequest request)
    {
        Member member = MemberMapper.INSTANCE.toMemberEntity(request);

        Member savedMember = memberService.save(member);
        savedMember.setPassword(passwordEncoder.encode(member.getPassword()));
        List<String> roles = authorityUtils.createRoles(request.getEmail());
        savedMember.setRoles(roles);
        return MemberMapper.INSTANCE.toMemberDetailResponse(savedMember);

    }

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


    public AuthenticationResponse login(String email)
    {
        String refreshToken = jwtService.generateRefreshToken(email);
        Member member = memberService.findMemberByEmail(email);
        redisRepository.saveRefreshToken(refreshToken, email);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .role(member.getRoles())
                .refreshToken(refreshToken)
                .build();

        return response;
    }

    public void logout(String refreshToken)
    {
        jwtService.isValidToken(refreshToken);

        if(hasKey(refreshToken))
        {
            redisRepository.deleteRefreshToken(refreshToken);
            return;
        }

        throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_FOUND);
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

        throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_FOUND);
    }

    public HttpHeaders setHeadersWithNewAccessToken(String newAccessToken)
    {
        HttpHeaders response = new HttpHeaders();
        response.set("Authorization", "Bearer " + newAccessToken);
        return response;
    }

    private Boolean hasKey(String refreshToken)
    {
        try{
            jwtService.isValidToken(refreshToken);
        }catch (RuntimeException ex){
            log.error(ex.getMessage(), ex);
            return false;
        }

        return redisTemplate.hasKey(refreshToken);
    }

}
