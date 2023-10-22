package com.jbaacount.visitor.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class VisitorInterceptor implements HandlerInterceptor
{
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String ipAddress = getIp(request);
        String userAgent = request.getHeader("User-Agent");
        LocalDate date = LocalDate.now();
        String key = ipAddress + "_" + date;

        ValueOperations valueOperations = redisTemplate.opsForValue();

        if(!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, userAgent);
        }

        log.info("ip address = {}", ipAddress);
        log.info("key = {}", key);
        return true;
    }

    private String getIp(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        return ip;
    }
}