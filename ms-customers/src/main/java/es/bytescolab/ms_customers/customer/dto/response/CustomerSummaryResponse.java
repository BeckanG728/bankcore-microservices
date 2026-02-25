package es.bytescolab.ms_customers.customer.dto.response;

import es.bytescolab.ms_customers.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryResponse {
    private UUID id;
    private String dni;
    private String fullName;    // firstName + " " + lastName
    private String email;
    private CustomerStatus status;
}
