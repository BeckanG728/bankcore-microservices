package es.bytescolab.ms_accounts.transaction.services;

import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse deposit(UUID accountId, UUID customerId, BigDecimal amount, String description);

    TransactionResponse withdraw(UUID accountId, UUID customerId, BigDecimal amount);

    List<TransactionResponse> getTransactionsByAccountId(UUID accountId, UUID customerId);
}
