package es.bytescolab.ms_accounts.account.controller;


import es.bytescolab.ms_accounts.account.dto.request.CreateAccountRequest;
import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;
import es.bytescolab.ms_accounts.account.dto.response.AccountSummaryResponse;
import es.bytescolab.ms_accounts.account.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestHeader("X-User-Id") UUID customerId,
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.info("POST /api/accounts — customerId: {}", customerId);
        AccountResponse response = accountService.createAccount(customerId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountSummaryResponse>> getAccountsByCustomerId(
            @RequestHeader("X-User-Id") UUID customerId
    ) {
        log.info("GET /api/accounts");
        List<AccountSummaryResponse> response = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @RequestHeader("X-User-Id") UUID customerId,
            @PathVariable UUID id
    ) {
        log.info("GET /api/accounts/{} — customerId: {}", id, customerId);
        AccountResponse response = accountService.getAccountById(id, customerId);
        return ResponseEntity.ok(response);
    }
}
