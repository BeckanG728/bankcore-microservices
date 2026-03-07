package es.bytescolab.ms_accounts.transfer.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull(message = "El ID de la cuenta origen es obligatorio")
        UUID originAccountId,

        @NotBlank(message = "El número de cuenta destino es obligatorio")
        String destinationAccountNumber,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        String description
) {
}
