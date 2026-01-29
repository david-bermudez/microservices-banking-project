package com.banking.cuenta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de reporte de estado de cuenta con información del cliente y sus cuentas")
public class ReporteEstadoCuentaDTO {

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String cliente;

    @Schema(description = "Número de identificación del cliente", example = "1234567890")
    private String identificacion;

    @Schema(description = "Fecha de inicio del reporte", example = "2024-01-01T00:00:00")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha de fin del reporte", example = "2024-01-31T23:59:59")
    private LocalDateTime fechaFin;

    @Schema(description = "Lista de cuentas del cliente con sus movimientos")
    private List<CuentaReporteDTO> cuentas;
}