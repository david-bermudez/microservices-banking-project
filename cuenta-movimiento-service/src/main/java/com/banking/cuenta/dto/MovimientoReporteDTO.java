package com.banking.cuenta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de un movimiento en el reporte")
public class MovimientoReporteDTO {

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