package es.bytescolab.ms_accounts.feign.fallback;

import es.bytescolab.ms_accounts.feign.CustomerFeignClient;
import es.bytescolab.ms_accounts.feign.dto.CustomerResponse;
import es.bytescolab.ms_accounts.feign.dto.CustomerValidationResponse;
import es.bytescolab.ms_accounts.utils.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CustomerFeignFallback implements CustomerFeignClient {

    @Override
    public CustomerValidationResponse validateCustomer(UUID customerId) {
        log.error("Fallback: No se pudo validar el cliente {} en ms-customers", customerId);
        throw new ExternalServiceException(
                "No se pudo conectar con el servicio de clientes. Intente nuevamente."
        );
    }

    @Override
    public CustomerResponse getCustomer(UUID customerId) {
        log.warn("Fallback: No se pudo obtener información del cliente {}", customerId);
        return null; // Retornar null para usar "Cliente externo" como fallback
    }
}
