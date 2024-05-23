package com.jbaacount.global.oauth2;

import com.jbaacount.service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
@RestController
public class OAuth2Controller
{
    private final OAuth2Service oAuth2Service;

    @Value("${frontend-uri}")
    private String REDIRECT_URI;

    @GetMapping("/code/{registrationId}")
    public void getGoogleUser(@RequestParam("code") String code,
                              @PathVariable("registrationId") String registrationId,
                              HttpServletResponse response) throws IOException
    {
        OAuth2Response data = oAuth2Service.oauth2Login(code, registrationId);

        // Set tokens in headers
        response.setHeader(AUTHORIZATION, "Bearer " + data.getAccessToken());
        response.setHeader("Refresh", data.getRefreshToken());

        // Redirect to frontend
        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI).toUriString();
        response.sendRedirect(redirectUrl);
    }
}
