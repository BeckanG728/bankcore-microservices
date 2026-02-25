package es.bytescolab.ms_customers.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerValidationResponse {
    private UUID customerId;
    boolean isExists;
    boolean isActive;
}
