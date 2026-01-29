package com.banking.cuenta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de la cuenta bancaria")
public class CuentaResponseDTO {

    @Schema(description = "ID interno de la cuenta", example = "1")
    private Long id;

    @Schema(description = "Número de cuenta bancaria", example = "478758")
    private String numeroCuenta;

    @Schema(description = "Tipo de cuenta", example = "Ahorro")
    private String tipoCuenta;

    @Schema(description = "Saldo inicial de la cuenta", example = "1000.00")
    private BigDecimal saldoInicial;

    @Schema(description = "Saldo actual de la cuenta", example = "1500.00")
    private BigDecimal saldoActual;

    @Schema(description = "Estado de la cuenta", example = "true")
    private Boolean estado;

    @Schema(description = "ID del cliente propietario", example = "1")
    private Long clienteId;

    @Schema(description = "Fecha y hora de creación", example = "2024-01-26T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2024-01-26T15:45:00")
    private LocalDateTime updatedAt;
}
