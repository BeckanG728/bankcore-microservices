package es.bytescolab.ms_customers.customer.service.impl;

import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerSummaryResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerValidationResponse;
import es.bytescolab.ms_customers.customer.mapper.CustomerProfileMapper;
import es.bytescolab.ms_customers.customer.repository.CustomerRepository;
import es.bytescolab.ms_customers.customer.service.CustomerService;
import es.bytescolab.ms_customers.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerProfileMapper customerProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerProfileResponse getMyProfile(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customerProfileMapper::toCustomerProfileResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + customerId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerValidationResponse validateCustomer(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    boolean isActive = customer.getStatus().name().equals("ACTIVE");
                    return new CustomerValidationResponse(true, isActive);
                })
                .orElse(new CustomerValidationResponse(false, false));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerSummaryResponse getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> CustomerSummaryResponse.builder()
                        .id(customer.getId())
                        .dni(customer.getDni())
                        .fullName(customer.getFirstName() + " " + customer.getLastName())
                        .email(customer.getEmail())
                        .status(customer.getStatus())
                        .build()
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un cliente con el ID proporcionado"
                ));
    }
}
