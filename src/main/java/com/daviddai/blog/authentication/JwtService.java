package com.daviddai.blog.authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.daviddai.blog.enums.TokenType;
import com.daviddai.blog.exception.AppException;
import com.daviddai.blog.exception.ErrorCode;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-ttl-ms}")
    private long accessTokenTtl;

    @Value("${jwt.refresh-ttl-ms}")
    private long refreshTokenTtl;

    @Value("${jwt.issuer}")
    private String issuer;

    // Extract Token
    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public String extractTokenType(Claims claims) {
        return claims.get("type").toString();
    }

    // Validate Token
    public void validateAccessToken(String token, UserDetails userDetails) {
        validateToken(token, userDetails, TokenType.ACCESS);
    }

    public void validateRefreshToken(String token, UserDetails userDetails) {
        validateToken(token, userDetails, TokenType.REFRESH);
    }

    private void validateToken(String token, UserDetails userDetails, TokenType expectedType) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        String tokenType = claims.get("type", String.class);

        if (subject == null || !subject.equals(userDetails.getUsername())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if (tokenType == null || !tokenType.equals(expectedType.toString())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    // Generate Token
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("role", roles.get(0));
        claims.put("type", TokenType.ACCESS.toString());
        return buildToken(claims, userDetails.getUsername(), accessTokenTtl);

    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", TokenType.REFRESH.toString());
        return buildToken(claims, userDetails.getUsername(), refreshTokenTtl);
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(issuer)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
