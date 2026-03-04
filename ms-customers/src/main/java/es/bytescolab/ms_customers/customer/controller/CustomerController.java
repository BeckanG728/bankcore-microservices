package es.bytescolab.ms_customers.customer.controller;

import es.bytescolab.ms_customers.auth.common.model.entity.UserEntity;
import es.bytescolab.ms_customers.customer.dto.request.UpdateProfileRequest;
import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerSummaryResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerValidationResponse;
import es.bytescolab.ms_customers.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/customers")
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
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal UserEntity currentUser,
                                         @Valid @RequestBody UpdateProfileRequest request) {
        CustomerProfileResponse updated = customerService.updateProfile(
                currentUser.getCustomerId(), request);
        return ResponseEntity.ok(updated);
    }
}
