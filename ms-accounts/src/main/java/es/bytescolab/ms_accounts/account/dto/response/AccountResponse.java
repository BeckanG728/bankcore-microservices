package es.bytescolab.ms_accounts.account.dto.response;

import es.bytescolab.ms_accounts.account.enums.AccountStatus;
import es.bytescolab.ms_accounts.account.enums.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record AccountResponse(
        UUID id,
        String accountNumber,
        UUID customerId,
        AccountType accountType,
        String currency,
        BigDecimal balance,
        String alias,
        AccountStatus status,
        Instant createdAt
) {
}
