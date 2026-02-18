package es.bytescolab.ms_customers.auth.common.mapper;

import es.bytescolab.ms_customers.auth.common.dto.request.RegisterRequest;
import es.bytescolab.ms_customers.customer.dto.RegisteredCustomer;
import es.bytescolab.ms_customers.customer.entity.Customer;
import es.bytescolab.ms_customers.customer.enums.CustomerStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    Customer toCustomer(RegisterRequest request);

    RegisteredCustomer toRegisteredCustomer(Customer customer);
}
