package es.bytescolab.ms_customers.auth.service.impl;

import es.bytescolab.ms_customers.auth.common.dto.request.LoginRequest;
import es.bytescolab.ms_customers.auth.common.dto.request.RegisterRequest;
import es.bytescolab.ms_customers.auth.common.dto.response.TokenResponse;
import es.bytescolab.ms_customers.auth.common.mapper.CustomerMapper;
import es.bytescolab.ms_customers.auth.common.mapper.UserMapper;
import es.bytescolab.ms_customers.auth.common.model.entity.UserEntity;
import es.bytescolab.ms_customers.auth.repository.UserRepository;
import es.bytescolab.ms_customers.auth.service.AuthService;
import es.bytescolab.ms_customers.auth.service.JwtService;
import es.bytescolab.ms_customers.customer.dto.RegisteredCustomer;
import es.bytescolab.ms_customers.customer.entity.Customer;
import es.bytescolab.ms_customers.customer.repository.CustomerRepository;
import es.bytescolab.ms_customers.utils.exception.InvalidCredentialsException; // Añadir import
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (customerRepository.existsByDni(request.dni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        Customer customer = customerMapper.toCustomer(request);
        Customer savedCustomer = customerRepository.save(customer);

        String encodedPassword = passwordEncoder.encode(request.password());

        UserEntity user = userMapper.toUserEntity(request, savedCustomer.getId());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        RegisteredCustomer customerDto = customerMapper.toRegisteredCustomer(savedCustomer);

        // Modificado para incluir customerId y UserRole
        TokenResponse tokenResponse = jwtService.generateToken(user.getId(), user.getEmail(), user.getCustomerId(), user.getRole());

        return TokenResponse.builder()
                .accessToken(tokenResponse.accessToken())
                .expiresIn(tokenResponse.expiresIn())
                .customer(customerDto)
                .build();
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado")); // Cambiado

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Contraseña inválida"); // Cambiado
        }

        Customer customer = customerRepository.findById(user.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer no encontrado"));

        RegisteredCustomer customerDto = customerMapper.toRegisteredCustomer(customer);

        // Modificado para incluir customerId y UserRole
        TokenResponse tokenResponse = jwtService.generateToken(user.getId(), user.getEmail(), user.getCustomerId(), user.getRole());

        return TokenResponse.builder()
                .accessToken(tokenResponse.accessToken())
                .expiresIn(tokenResponse.expiresIn())
                .customer(customerDto)
                .build();
    }
}
