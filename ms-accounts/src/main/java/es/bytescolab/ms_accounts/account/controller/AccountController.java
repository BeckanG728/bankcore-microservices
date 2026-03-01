package es.bytescolab.ms_accounts.account.controller;


import es.bytescolab.ms_accounts.account.dto.request.CreateAccountRequest;
import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;
import es.bytescolab.ms_accounts.account.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
