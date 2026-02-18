package es.bytescolab.ms_customers.auth.common.dto.response;

import es.bytescolab.ms_customers.customer.dto.RegisteredCustomer;
import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        long expiresIn,
        RegisteredCustomer customer
) {
}
