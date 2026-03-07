package es.bytescolab.ms_accounts.transaction.controller;

import es.bytescolab.ms_accounts.transaction.dto.request.DepositRequest;
import es.bytescolab.ms_accounts.transaction.dto.request.WithdrawalRequest;
import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;
import es.bytescolab.ms_accounts.transaction.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposits")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestHeader("X-User-Id") UUID customerId,
            @PathVariable UUID accountId,
            @Valid @RequestBody DepositRequest request
    ) {
        log.info("POST /api/accounts/{}/transactions/deposits — customerId: {}", accountId, customerId);
        TransactionResponse response = transactionService.deposit(accountId, customerId, request.amount(), request.description());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestHeader("X-User-Id") UUID customerId,
            @PathVariable UUID accountId,
            @Valid @RequestBody WithdrawalRequest request
    ) {
        log.info("POST /api/accounts/{}/transactions/withdrawals — customerId: {}", accountId, customerId);
        TransactionResponse response = transactionService.withdraw(accountId, customerId, request.amount());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @RequestHeader("X-User-Id") UUID customerId,
            @PathVariable UUID accountId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(required = false) String type
    ) {
        log.info("GET /api/accounts/{}/transactions/history — customerId: {}", accountId, customerId);
        Page<TransactionResponse> response = transactionService.getTransactionHistory(
                accountId, customerId, page, size, startDate, endDate, type
        );
        return ResponseEntity.ok(response);
    }
}
