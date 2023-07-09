package com.jbaacount.global.security.jwt;

import com.jbaacount.global.security.userdetails.MemberDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService
{
    private final Key secretKey;

    private final int accessTokenExpiration;

    private final int refreshTokenExpiration;
    private final MemberDetailsService memberDetailsService;

    public JwtService(@Value("${jwt.key}")String secretKey,
                      @Value("${jwt.access-token-expiration-minutes}")int accessTokenExpiration,
                      @Value("${jwt.refresh-token-expiration-minutes}")int refreshTokenExpiration, UserDetailsService userDetailsService, MemberDetailsService memberDetailsService)
    {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.memberDetailsService = memberDetailsService;
    }

    public String generateAccessToken(Authentication authentication)
    {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpirationDate(accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken()
    {
        return Jwts.builder()
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpirationDate(refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken)
    {
        Claims claims = getClaims(accessToken);
        String email = claims.getSubject();
        List<String> roles = (List<String>) claims.get("roles");

        if(roles == null)
            throw new RuntimeException("token does not have any roles");

        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return authentication;
    }

    public Claims getClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Date getTokenExpirationDate(int expirationMinutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);

        return calendar.getTime();
    }

    public boolean isValidToken(String token)
    {
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        return true;
    }

}
