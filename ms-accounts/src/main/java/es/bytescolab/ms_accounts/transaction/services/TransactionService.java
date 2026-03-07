package es.bytescolab.ms_accounts.transaction.services;

import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse deposit(UUID accountId, UUID customerId, BigDecimal amount, String description);

    TransactionResponse withdraw(UUID accountId, UUID customerId, BigDecimal amount);

    Page<TransactionResponse> getTransactionHistory(
            UUID accountId,
            UUID customerId,
            Integer page,
            Integer size,
            Instant startDate,
            Instant endDate,
            String type
    );
}
