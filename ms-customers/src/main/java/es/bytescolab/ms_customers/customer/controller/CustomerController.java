package es.bytescolab.ms_customers.customer.controller;

import es.bytescolab.ms_customers.auth.common.model.entity.UserEntity;
import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getCustomer() {
        return null;
    }

    @GetMapping("/{customerId}/validate")
    public ResponseEntity<?> validateCustomer() {
        return null;
    }

    @PutMapping("/me")
    public ResponseEntity<?> editProfile() {
        return null;
    }
}
