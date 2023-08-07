package com.jbaacount.global.handler;

import com.esotericsoftware.kryo.util.ObjectMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbaacount.global.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    }
}
