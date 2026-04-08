package com.college.student.portal.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "jEXRHqfmSjtn5R8WdOjmJDFnPnK5J7Qsgtreg5UHWZZImMAPHF+5+voBYvtSkV1XHdf2HpmOOehSS8JOtpltVg==";

    private final long TOKEN_EXPIRY_DURATION = 60 * 60 * 1000; // 60 minutes

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_DURATION))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiry = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiry.before(new Date());
    }

    public boolean isValidToken(String token, String username) {
        String usernameFromToken = getUsernameFromToken(token);
        return usernameFromToken.equalsIgnoreCase(username) && !isTokenExpired(token);
    }
}