package es.bytescolab.ms_accounts.transfer.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
        UUID id,
        UUID originAccountId,
        String originAccountNumber,
        UUID destinationAccountId,
        String destinationAccountNumber,
        String destinationAccountHolderName,
        BigDecimal amount,
        String description,
        Instant createdAt
) {
}
