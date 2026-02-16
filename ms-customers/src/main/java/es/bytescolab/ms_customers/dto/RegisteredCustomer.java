package es.bytescolab.ms_customers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredCustomer {
    private String id;
    private String dni;
    private String fullName;
    private String email;
    private Enum status;
    private LocalDateTime createdAt;
}
