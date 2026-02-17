package es.bytescolab.ms_customers.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        return null;
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
