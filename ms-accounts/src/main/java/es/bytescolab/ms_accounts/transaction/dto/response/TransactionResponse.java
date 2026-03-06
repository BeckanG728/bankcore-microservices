package es.bytescolab.ms_accounts.transaction.dto.response;

import es.bytescolab.ms_accounts.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        TransactionType type,
        BigDecimal amount,
        String description,
        Instant createdAt
) {
}
