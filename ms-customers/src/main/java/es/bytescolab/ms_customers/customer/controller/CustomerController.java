package es.bytescolab.ms_customers.customer.controller;

import es.bytescolab.ms_customers.auth.common.model.entity.UserEntity;
import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerSummaryResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerValidationResponse;
import es.bytescolab.ms_customers.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserEntity currentUser) {
        CustomerProfileResponse profile = customerService.getMyProfile(currentUser.getCustomerId());
        return ResponseEntity.ok(profile);
    }


    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerSummaryResponse> getCustomer(@PathVariable UUID customerId) {
        CustomerSummaryResponse summary = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{customerId}/validate")
    public ResponseEntity<CustomerValidationResponse> validateCustomer(@PathVariable UUID customerId) {
        CustomerValidationResponse response = customerService.validateCustomer(customerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<?> editProfile() {
        return null;
    }
}
