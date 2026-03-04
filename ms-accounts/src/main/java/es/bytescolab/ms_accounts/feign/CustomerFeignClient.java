package es.bytescolab.ms_accounts.feign;

import es.bytescolab.ms_accounts.feign.dto.CustomerResponse;
import es.bytescolab.ms_accounts.feign.dto.CustomerValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-customers", url = "${customers.service.url}")
public interface CustomerFeignClient {

    @GetMapping("/api/customers/{customerId}/validate")
    CustomerValidationResponse validateCustomer(@PathVariable UUID customerId);

    @GetMapping("/api/customers/{customerId}")
    CustomerResponse getCustomer(@PathVariable UUID customerId);

}
