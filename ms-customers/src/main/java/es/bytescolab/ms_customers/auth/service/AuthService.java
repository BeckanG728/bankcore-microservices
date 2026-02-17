package es.bytescolab.ms_customers.auth.service;

import es.bytescolab.ms_customers.auth.common.dto.request.LoginRequest;
import es.bytescolab.ms_customers.auth.common.dto.request.RegisterRequest;
import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;


public interface AuthService {
    TokenResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}
