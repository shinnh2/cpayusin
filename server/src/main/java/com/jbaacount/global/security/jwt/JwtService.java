package com.jbaacount.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Slf4j
@Getter
@Component
public class JwtService
{
    private SecretKey secretKey;
    private int accessTokenExpirationMinutes;
    private int refreshTokenExpirationMinutes;

    public JwtService(@Value("${jwt.key}")String secretKey,
                        @Value("${jwt.access-token-expiration-minutes}")int accessTokenExpirationMinutes,
                        @Value("${jwt.refresh-token-expiration-minutes}")int refreshTokenExpirationMinutes)
    {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    public String encodedBase64SecretKey(String secretKey)
    {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, List<String> roles)
    {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpiration(accessTokenExpirationMinutes))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email)
    {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(getTokenExpiration(refreshTokenExpirationMinutes))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserEmail(String jws)
    {
        String email = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();

        return email;
    }

    public Claims getClaims(String jws)
    {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jws)
                .getBody();

        return claims;
    }

    public boolean isValidToken(String jws)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jws);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SignatureException e) {
            log.error("there is an error with the signature of you token ");
        }
        return false;
    }

    public Date getTokenExpiration(int expirationMinutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey)
    {
        byte keyBytes[] = Decoders.BASE64.decode(base64EncodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
}

