package es.bytescolab.ms_accounts.account.dto.request;

import es.bytescolab.ms_accounts.account.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateAccountRequest(
        @NotNull(message = "El tipo de cuenta es obligatorio")
        AccountType accountType,

        @NotBlank(message = "La moneda es obligatoria")
        @Pattern(regexp = "^[A-Z]{3}$", message = "La moneda debe ser un código ISO de 3 letras (ej: EUR, USD)")
        String currency,

        @Size(max = 100, message = "El alias no puede superar los 100 caracteres")
        String alias
) {
}
