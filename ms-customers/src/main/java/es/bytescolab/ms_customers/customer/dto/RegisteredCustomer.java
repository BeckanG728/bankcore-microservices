package es.bytescolab.ms_customers.customer.dto;

import es.bytescolab.ms_customers.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredCustomer {
    private UUID id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private CustomerStatus status;
    private Instant createdAt;
}
