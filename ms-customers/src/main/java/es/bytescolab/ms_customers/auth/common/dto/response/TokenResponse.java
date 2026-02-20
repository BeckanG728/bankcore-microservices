package es.bytescolab.ms_customers.auth.common.dto.response;

import es.bytescolab.ms_customers.customer.dto.request.RegisteredCustomer;
import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        long expiresIn,
        RegisteredCustomer customer
) {
}
