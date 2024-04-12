package com.jbaacount.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.exception.InvalidTokenException;
import com.jbaacount.global.oauth2.OAuth2Response;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.mapper.MemberMapper;
import com.jbaacount.model.File;
import com.jbaacount.model.Member;
import com.jbaacount.model.Platform;
import com.jbaacount.payload.request.MemberRegisterRequest;
import com.jbaacount.payload.response.AuthenticationResponse;
import com.jbaacount.payload.response.MemberDetailResponse;
import com.jbaacount.repository.RedisRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping("/api/v1/auth")
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
    private final FileService fileService;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();
    public final String BASIC_URL = "oauth2.";
    public final String GOOGLE_GRANT_TYPE = "authorization_code";


    @Transactional
    public MemberDetailResponse register(MemberRegisterRequest request)
    {
        Member member = MemberMapper.INSTANCE.toMemberEntity(request);
        member.updatePassword(passwordEncoder.encode(request.getPassword()));
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

    public OAuth2Response oauth2Login(String code, String registrationId)
    {
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResource = getUserInfoFromToken(accessToken, registrationId);

        return userLoginProcess(userResource, registrationId);
    }


    private String getAccessToken(String code, String registrationId)
    {
        String clientId = env.getProperty(BASIC_URL + registrationId + ".client-id");
        String clientSecret = env.getProperty(BASIC_URL + registrationId + ".client-secret");
        String redirectUri = env.getProperty(BASIC_URL + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty(BASIC_URL + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", GOOGLE_GRANT_TYPE);
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<JsonNode> exchange = restTemplate.exchange(tokenUri, HttpMethod.POST, httpEntity, JsonNode.class);

        return exchange.getBody().get("access_token").asText();
    }

    public JsonNode getUserInfoFromToken(String accessToken, String registrationId)
    {
        String resourceUri = env.getProperty(BASIC_URL + registrationId + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    public OAuth2Response userLoginProcess(JsonNode resource, String registrationId) {
        switch (registrationId) {
            case "google":
                return processGoogleLogin(resource);
            default:
                throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
        }
    }

    private OAuth2Response processGoogleLogin(JsonNode resource) {
        String name = resource.get("name").asText();
        String email = resource.get("email").asText();
        String picture = resource.get("picture").asText();

        String accessToken = jwtService.generateAccessToken(email, List.of("USER"));
        String refreshToken = jwtService.generateRefreshToken(email);

        setHeadersWithNewAccessToken(accessToken);

        return saveOrUpdate(name, email, picture, "google");
    }

    private OAuth2Response saveOrUpdate(String name, String email, String picture, String registrationId) {
        Member member = memberService.findOptionalMemberByEmail(email)
                .orElseGet(() -> {
                    Member newMember = new Member(name, email, Platform.valueOf(registrationId));
                    Member savedMember = memberService.save(newMember);
                    File file = fileService.save(File.builder().url(picture).build());
                    file.addMember(savedMember);
                    return newMember;
                });

        member.setNickname(name);

        return OAuth2Response.builder().build();
    }


    public String logout(String refreshToken)
    {
        jwtService.isValidToken(refreshToken);

        if(!redisRepository.hasKey(refreshToken))
            throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_FOUND);

        redisRepository.deleteRefreshToken(refreshToken);
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
        response.set("Authorization", "Bearer " + newAccessToken);
        return response;
    }


}
