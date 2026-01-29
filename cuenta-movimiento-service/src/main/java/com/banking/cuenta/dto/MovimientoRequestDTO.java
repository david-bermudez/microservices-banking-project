package com.banking.cuenta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para registrar un nuevo movimiento bancario")
public class MovimientoRequestDTO {

    @Schema(description = "ID de la cuenta donde se registra el movimiento", example = "1")
    @NotNull(message = "El cuentaId es obligatorio")
    private Long cuentaId;

    @Schema(description = "Tipo de movimiento", example = "Deposito")
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // Deposito, Retiro

    @Schema(description = "Valor del movimiento (positivo para depósito, negativo para retiro)", example = "100.00")
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor; // Positivo para depósito, negativo para retiro

    @Schema(description = "Descripción opcional del movimiento", example = "Depósito en efectivo")
    private String descripcion;
}
