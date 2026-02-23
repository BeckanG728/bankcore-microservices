package es.bytescolab.ms_customers.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerValidationResponse {
    boolean isExists;
    boolean isActive;
}
