package com.jbaacount.global.security.dto;

import com.jbaacount.member.entity.Platform;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes
{
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private Platform platform;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email, Platform platform)
    {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.platform = platform;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes)
    {
        switch (registrationId.toLowerCase())
        {
            case "kakao":
                return ofKakao("id", attributes);

            case "naver":
                return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes)
    {
        return OAuthAttributes.builder()
                .nickname((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .platform(Platform.GOOGLE)
                .build();
    }

    public static OAuthAttributes ofNaver(String usernameAttributeName, Map<String, Object> attributes)
    {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .nickname((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(usernameAttributeName)
                .platform(Platform.NAVER)
                .build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes)
    {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .nickname((String) profile.get("nickname"))
                .email((String) (kakaoAccount.get("email")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .platform(Platform.KAKAO)
                .build();
    }
}
