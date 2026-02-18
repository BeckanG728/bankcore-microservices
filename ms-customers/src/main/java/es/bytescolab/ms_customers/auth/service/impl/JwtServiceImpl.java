package es.bytescolab.ms_customers.auth.service.impl;

import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;
import es.bytescolab.ms_customers.auth.common.model.enums.UserRole; // Añadir import
import es.bytescolab.ms_customers.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret:mySecretKeyForJWTGenerationWithMinimumLengthAndStrongRandomCharacters}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public TokenResponse generateToken(UUID userId, String userEmail, UUID customerId, UserRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("userEmail", userEmail);
        claims.put("customerId", customerId.toString()); // Añadir customerId
        claims.put("role", role.name()); // Añadir role

        String accessToken = Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(expiration / 1000)
                .build();
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date());
    }

    @Override
    public UUID extractUserId(String token) {
        Claims claims = getClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    @Override
    public String extractUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.get("userEmail", String.class);
    }

    @Override
    public UUID extractCustomerId(String token) {
        Claims claims = getClaims(token);
        return UUID.fromString(claims.get("customerId", String.class));
    }

    @Override
    public UserRole extractUserRole(String token) {
        Claims claims = getClaims(token);
        return UserRole.valueOf(claims.get("role", String.class));
    }
}
