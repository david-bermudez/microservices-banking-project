package com.banking.cuenta.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de una cuenta en el reporte")
public class CuentaReporteDTO {

        @Schema(description = "Número de cuenta", example = "478758")
        private String numeroCuenta;

        @Schema(description = "Tipo de cuenta", example = "Ahorro")
        private String tipoCuenta;

        @Schema(description = "Saldo inicial de la cuenta", example = "1000.00")
        private BigDecimal saldoInicial;

        @Schema(description = "Saldo actual/final de la cuenta", example = "1500.00")
        private BigDecimal saldoActual;

        @Schema(description = "Estado de la cuenta", example = "true")
        private Boolean estado;

        @Schema(description = "Lista de movimientos de la cuenta en el período")
        private List<MovimientoReporteDTO> movimientos;
}