package es.bytescolab.ms_customers.customer.service;

import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerSummaryResponse;
import es.bytescolab.ms_customers.customer.dto.response.CustomerValidationResponse;

import java.util.UUID;

public interface CustomerService {

    CustomerProfileResponse getMyProfile(UUID customerId);

    CustomerValidationResponse validateCustomer(UUID customerId);

    CustomerSummaryResponse getCustomerById(UUID customerId);
}
