package com.jbaacount.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomAuthenticationSuccessfulHandler implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        log.info("# Authenticated successfully");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.print("{\"message\":\"Authenticated successfully\"}");
        printWriter.flush();
    }
}
