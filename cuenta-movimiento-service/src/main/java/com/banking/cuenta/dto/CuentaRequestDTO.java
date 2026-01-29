package com.banking.cuenta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear o actualizar una cuenta bancaria")
public class CuentaRequestDTO {

    @Schema(description = "Número de cuenta bancaria", example = "478758")
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
    private String numeroCuenta;

    @Schema(description = "Tipo de cuenta", example = "Ahorro", allowableValues = { "Ahorro", "Corriente" })
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(Ahorro|Corriente)$", message = "El tipo de cuenta debe ser 'Ahorro' o 'Corriente'")
    private String tipoCuenta;

    @Schema(description = "Saldo inicial de la cuenta", example = "1000.00")
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;

    @Schema(description = "Estado de la cuenta (activa/inactiva)", example = "true", defaultValue = "true")
    @Builder.Default
    private Boolean estado = true;

    @Schema(description = "ID del cliente propietario de la cuenta", example = "1")
    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;
}
