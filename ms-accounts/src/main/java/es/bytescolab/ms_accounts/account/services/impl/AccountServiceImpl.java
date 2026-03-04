package es.bytescolab.ms_accounts.account.services.impl;

import es.bytescolab.ms_accounts.account.dto.request.CreateAccountRequest;
import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;
import es.bytescolab.ms_accounts.account.entity.Account;
import es.bytescolab.ms_accounts.account.enums.AccountStatus;
import es.bytescolab.ms_accounts.account.repository.AccountRepository;
import es.bytescolab.ms_accounts.account.services.AccountService;
import es.bytescolab.ms_accounts.feign.CustomerFeignClient;
import es.bytescolab.ms_accounts.feign.dto.CustomerValidationResponse;
import es.bytescolab.ms_accounts.utils.exception.CustomerInactiveException;
import es.bytescolab.ms_accounts.utils.exception.CustomerNotFoundException;
import es.bytescolab.ms_accounts.utils.exception.MaxAccountsReachedException;
import es.bytescolab.ms_accounts.utils.math.IbanGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final int MAX_ACCOUNTS_PER_CUSTOMER = 3;

    private final AccountRepository accountRepository;
    private final CustomerFeignClient customerFeignClient;

    @Override
    @Transactional
    public AccountResponse createAccount(UUID customerId, CreateAccountRequest request) {

        // 1. Validar cliente vía ms-customers (llamada Feign a través de Eureka)
        log.info("Validando cliente {} en ms-customers", customerId);
        CustomerValidationResponse validation = customerFeignClient.validateCustomer(customerId);

        if (!validation.exists()) {
            log.warn("Cliente {} no encontrado en ms-customers", customerId);
            throw new CustomerNotFoundException(
                    "No existe un cliente con el ID proporcionado"
            );
        }

        if (!validation.isActive()) {
            log.warn("Cliente {} existe pero no está activo", customerId);
            throw new CustomerInactiveException(
                    "El cliente no está activo y no puede crear nuevas cuentas"
            );
        }

        // 2. Validar límite de 3 cuentas por cliente (excluye cuentas CLOSED)
        long activeAccountCount = accountRepository.countByCustomerIdAndStatusNot(
                customerId, AccountStatus.CLOSED
        );
        log.info("Cliente {} tiene {} cuentas activas", customerId, activeAccountCount);

        if (activeAccountCount >= MAX_ACCOUNTS_PER_CUSTOMER) {
            throw new MaxAccountsReachedException(
                    "El cliente ya ha alcanzado el máximo de " + MAX_ACCOUNTS_PER_CUSTOMER + " cuentas"
            );
        }

        // 3. Generar IBAN único
        String iban = generateUniqueIban();
        log.info("IBAN generado para cliente {}: {}", customerId, iban);

        // 4. Crear cuenta con saldo 0
        Account account = Account.builder()
                .customerId(customerId)
                .accountNumber(iban)
                .accountType(request.accountType())
                .currency(request.currency())
                .alias(request.alias())
                .build();
        // balance y status tienen @Builder.Default: 0.00 y ACTIVE

        Account savedAccount = accountRepository.save(account);
        log.info("Cuenta creada con ID: {} para cliente: {}", savedAccount.getId(), customerId);

        return toResponse(savedAccount);
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .accountType(account.getAccountType())
                .currency(account.getCurrency())
                .balance(account.getBalance().setScale(2, java.math.RoundingMode.HALF_UP))
                .alias(account.getAlias())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }

    // Genera un IBAN único, reintentando hasta 5 veces en caso de colisión.
    private String generateUniqueIban() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String iban = IbanGenerator.generate();
            if (!accountRepository.existsByAccountNumber(iban)) {
                return iban;
            }
            log.warn("Colisión de IBAN en intento {}, regenerando...", attempt + 1);
        }
        throw new IllegalStateException("No se pudo generar un IBAN único después de 5 intentos");
    }
}
