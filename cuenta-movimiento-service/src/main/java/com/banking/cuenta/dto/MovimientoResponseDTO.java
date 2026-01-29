package com.banking.cuenta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información del movimiento bancario")
public class MovimientoResponseDTO {

    @Schema(description = "ID interno del movimiento", example = "1")
    private Long id;

    @Schema(description = "ID de la cuenta asociada", example = "1")
    private Long cuentaId;

    @Schema(description = "Número de cuenta", example = "478758")
    private String numeroCuenta;

    @Schema(description = "Fecha y hora del movimiento", example = "2024-01-26T10:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Tipo de movimiento", example = "Deposito")
    private String tipoMovimiento;

    @Schema(description = "Valor del movimiento", example = "100.00")
    private BigDecimal valor;

    @Schema(description = "Saldo antes del movimiento", example = "1000.00")
    private BigDecimal saldoAnterior;

    @Schema(description = "Saldo después del movimiento", example = "1100.00")
    private BigDecimal saldoNuevo;

    @Schema(description = "Descripción del movimiento", example = "Depósito en efectivo")
    private String descripcion;
}
