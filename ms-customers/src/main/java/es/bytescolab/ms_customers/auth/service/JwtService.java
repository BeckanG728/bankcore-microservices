package es.bytescolab.ms_customers.auth.service;

import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {

    TokenResponse generateToken(UUID userId);
    Claims getClaims(String token);
    boolean isExpired(String token);
    UUID extractUserId(String token);

}
