package com.jbaacount.global.service;

import com.jbaacount.global.security.dto.OAuthAttributes;
import com.jbaacount.global.security.jwt.JwtService;
import com.jbaacount.global.security.utiles.CustomAuthorityUtils;
import com.jbaacount.model.Member;
import com.jbaacount.model.Platform;
import com.jbaacount.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        log.info("===OAuth2Service===");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        String accessToken = jwtService.generateAccessToken(member.getEmail(), member.getRoles());
        String refreshToken = jwtService.generateRefreshToken(member.getEmail());

        OAuth2UserAuthority userAuthority = new OAuth2UserAuthority(attributes.getAttributes());

        DefaultOAuth2User user = new DefaultOAuth2User(
                Collections.singleton(userAuthority),
                attributes.getAttributes(),
                userNameAttributeName);

        return user;
    }

    private Member saveOrUpdate(OAuthAttributes attributes)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(attributes.getEmail());

        //if present = existing member
        if(optionalMember.isPresent())
        {
            return optionalMember.get();
        }

        // else = should sign up first
        else
        {
            String email = attributes.getEmail();
            String nickname = attributes.getNickname();
            List<String> roles = authorityUtils.createRoles(email);
            Platform platform = attributes.getPlatform();

            Member member = new Member(nickname, email, roles, platform);

            return memberRepository.save(member);
        }
    }

}
