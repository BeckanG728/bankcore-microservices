package es.bytescolab.ms_customers.customer.service;

import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;

import java.util.UUID;

public interface CustomerService {

    CustomerProfileResponse getMyProfile(UUID customerId);
}
