package es.bytescolab.ms_customers.auth.service;

import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;
import es.bytescolab.ms_customers.auth.common.model.enums.UserRole; // Importar UserRole
import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {

    // Modificar generateToken para incluir customerId y UserRole
    TokenResponse generateToken(UUID userId, String userEmail, UUID customerId, UserRole role);

    Claims getClaims(String token);
    boolean isExpired(String token);
    UUID extractUserId(String token);
    String extractUserEmail(String token);

    // Añadir métodos para extraer customerId y UserRole
    UUID extractCustomerId(String token);
    UserRole extractUserRole(String token);
}
