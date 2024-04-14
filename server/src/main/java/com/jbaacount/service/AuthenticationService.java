package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.exception.InvalidTokenException;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.model.Member;
import com.jbaacount.model.type.Platform;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.AuthenticationResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.repository.RedisRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthenticationService
{
    private final MemberService memberService;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisRepository redisRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public MemberDetailResponse register(MemberRegisterRequest request)
    {
        Member member = MemberMapper.INSTANCE.toMemberEntity(request);
        member.updatePassword(passwordEncoder.encode(request.getPassword()));
        member.setPlatform(Platform.HOME);
        List<String> roles = authorityUtils.createRoles(request.getEmail());
        member.setRoles(roles);
        Member savedMember = memberService.save(member);

        return MemberMapper.INSTANCE.toMemberDetailResponse(savedMember);

    }

    public String verifyCode(String email, String inputCode)
    {
        log.info("email = {}", email);
        String verificationCode = redisRepository.getVerificationCodeByEmail(email);
        log.info("verification code = {}", verificationCode);

        if(verificationCode == null)
            throw new BusinessLogicException(ExceptionMessage.EXPIRED_VERIFICATION_CODE);

        if(verificationCode.equals(inputCode))
        {
            redisRepository.deleteEmailAfterVerification(email);
            log.info("email removed from redis successfully");
            return "인증이 완료되었습니다.";
        }


        throw new BusinessLogicException(ExceptionMessage.INVALID_VERIFICATION_CODE);
    }

    @Transactional
    public MemberDetailResponse resetPassword(String email, String password)
    {
        Member member = memberService.findMemberByEmail(email);

        member.updatePassword(passwordEncoder.encode(password.toString()));

        return MemberMapper.INSTANCE.toMemberDetailResponse(member);
    }


    public String logout(String refreshToken)
    {
        jwtService.isValidToken(refreshToken);

        if(!redisRepository.hasKey(refreshToken))
            throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_FOUND);

        redisRepository.deleteRefreshToken(refreshToken);
        SecurityContextHolder.clearContext();
        return "로그아웃에 성공했습니다";
    }

    public AuthenticationResponse reissue(String accessToken, String refreshToken)
    {
        if(redisRepository.hasKey(refreshToken))
        {
            log.info("===reissue===");
            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            Claims claims = jwtService.getClaims(accessToken.substring(7));
            String email = claims.getSubject();

            Member member = memberService.findMemberByEmail(email);

            String renewedAccessToken = jwtService.generateAccessToken(email, member.getRoles());


            redisRepository.saveRefreshToken(refreshToken, email);

            return AuthenticationResponse.builder()
                    .memberId(member.getId())
                    .email(email)
                    .role(member.getRoles())
                    .accessToken(renewedAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_FOUND);
    }

    public HttpHeaders setHeadersWithNewAccessToken(String newAccessToken)
    {
        HttpHeaders response = new HttpHeaders();
        response.set(AUTHORIZATION, "Bearer " + newAccessToken);
        return response;
    }
}
