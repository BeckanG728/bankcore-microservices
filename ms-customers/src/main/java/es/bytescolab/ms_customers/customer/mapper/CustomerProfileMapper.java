package es.bytescolab.ms_customers.customer.mapper;


import es.bytescolab.ms_customers.customer.dto.response.CustomerProfileResponse;
import es.bytescolab.ms_customers.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerProfileMapper {
    CustomerProfileResponse toCustomerProfileResponse(Customer customer);
}
