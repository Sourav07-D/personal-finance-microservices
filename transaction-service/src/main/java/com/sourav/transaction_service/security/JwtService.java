package com.sourav.transaction_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ============================
    // Extract Email
    // ============================

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ============================
    // Extract Role
    // ============================

    public String extractRole(String token) {
        return extractAllClaims(token)
                .get("role", String.class);
    }

    // ============================
    // Extract Expiration
    // ============================

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ============================
    // Generic Claim Extractor
    // ============================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // ============================
    // Validate Token
    // ============================

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    // ============================
    // Expiration Check
    // ============================

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    // ============================
    // Parse Claims
    // ============================

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}