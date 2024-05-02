package com.jbaacount.global.security.jwt;

import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.global.exception.InvalidTokenException;
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

    public String generateAccessToken(String email)
    {
        return Jwts.builder()
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
            throw new InvalidTokenException(ExceptionMessage.TOKEN_NOT_VALID);
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException(ExceptionMessage.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new InvalidTokenException(ExceptionMessage.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException(ExceptionMessage.CLAIM_EMPTY);
        } catch (SignatureException e) {
            throw new InvalidTokenException(ExceptionMessage.INVALID_TOKEN_SIGNATURE);
        }
    }

    public Date getTokenExpiration(int expirationMinutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }
}

