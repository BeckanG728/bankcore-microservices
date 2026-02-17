package es.bytescolab.ms_customers.auth.service.impl;

import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;
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

    @Value("${jwt.secret:mySecretKeyForJWTGenerationWithMinimumLength}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public TokenResponse generateToken(UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());

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
}
