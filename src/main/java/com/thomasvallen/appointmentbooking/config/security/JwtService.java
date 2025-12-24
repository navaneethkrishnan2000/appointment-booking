package com.thomasvallen.appointmentbooking.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private final TokenBlacklistService tokenBlacklistService;

    public JwtService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public SecretKey getSigningKey() {
        return secretKey;
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaims(token, claims -> claims.get("email", String.class));
    }

    public String extractRole(String token) {
        return extractClaims(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public Instant extractExpiry(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate != null ? expirationDate.toInstant(): null;
    }

    public <T> T extractClaims(
            String token, Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    @Nullable
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("❌ Invalid JWT Token: " + e.getMessage());
            return null;
        }
    }

    public String generateAccessToken(@NotNull UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("role", role);

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds( 60 * 60 * 24 * 7L); // 7 days
        return buildToken(claims, userDetails, now, expiration);
    }

    public String generateRefreshToken(@NotNull UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("role", role);

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(60 * 60 * 24 * 14L); // 14 days
        return buildToken(claims, userDetails, now, expiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            @NotNull UserDetails userDetails,
            Instant now,
            Instant expiration
    ) {
        return Jwts.builder()
                .setClaims(Optional.ofNullable(extraClaims).orElse(new HashMap<>()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            return false;
        }

        final String username = extractUsername(token);
        assert username != null;
        return username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        assert expiration != null;
        return expiration.toInstant().isBefore(Instant.now());
    }


}
