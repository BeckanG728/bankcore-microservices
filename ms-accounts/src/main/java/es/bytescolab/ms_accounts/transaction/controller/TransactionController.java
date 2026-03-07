package es.bytescolab.ms_accounts.transaction.controller;

import es.bytescolab.ms_accounts.transaction.dto.request.DepositRequest;
import es.bytescolab.ms_accounts.transaction.dto.request.WithdrawalRequest;
import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;
import es.bytescolab.ms_accounts.transaction.services.TransactionService;
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
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestHeader("X-User-Id") UUID customerId,
            @PathVariable UUID accountId
    ) {
        log.info("GET /api/accounts/{}/transactions — customerId: {}", accountId, customerId);
        List<TransactionResponse> response = transactionService.getTransactionsByAccountId(accountId, customerId);
        return ResponseEntity.ok(response);
    }
}
