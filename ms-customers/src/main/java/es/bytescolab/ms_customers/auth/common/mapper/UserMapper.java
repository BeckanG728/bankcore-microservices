package es.bytescolab.ms_customers.auth.common.mapper;

import es.bytescolab.ms_customers.auth.common.dto.request.RegisterRequest;
import es.bytescolab.ms_customers.auth.common.model.entity.UserEntity;
import es.bytescolab.ms_customers.auth.common.model.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    UserEntity toUserEntity(RegisterRequest request, UUID customerId);
}
