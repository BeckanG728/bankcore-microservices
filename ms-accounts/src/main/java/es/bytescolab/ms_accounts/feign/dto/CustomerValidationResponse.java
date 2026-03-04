package es.bytescolab.ms_accounts.feign.dto;

import java.util.UUID;

public record CustomerValidationResponse(
        UUID customerId,
        boolean exists,
        boolean isActive
) {
}
