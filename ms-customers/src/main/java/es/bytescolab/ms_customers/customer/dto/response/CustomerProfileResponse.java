package es.bytescolab.ms_customers.customer.dto.response;


import es.bytescolab.ms_customers.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CustomerProfileResponse {
    private UUID id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;
    private Instant createdAt;
}
