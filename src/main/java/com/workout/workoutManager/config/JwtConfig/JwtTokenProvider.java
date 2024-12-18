package com.workout.workoutManager.config.JwtConfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, accessTokenValidityInMilliseconds);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId, String email) {
        return createToken(userId, email, refreshTokenValidityInMilliseconds);
    }

    // 토큰 생성 메소드
    private String createToken(Long userId, String email, long validityInMilliseconds) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("email", email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 userId 추출
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // 토큰에서 email 추출
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Claims 파싱
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}