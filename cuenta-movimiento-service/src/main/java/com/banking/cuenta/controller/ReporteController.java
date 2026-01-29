package com.banking.cuenta.controller;

import com.banking.cuenta.dto.ReporteEstadoCuentaDTO;
import com.banking.cuenta.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes", description = "API para la generación de reportes bancarios")
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * F4: Endpoint para generar reporte de estado de cuenta
     * Formato: /reportes?fecha=01/02/2022&cliente=1
     * O con rango: /reportes?fecha=01/02/2022-10/02/2022&cliente=1
     */
    @Operation(summary = "Generar reporte de estado de cuenta", description = "Genera un reporte de estado de cuenta para un cliente en una fecha específica o rango de fechas. "
            +
            "Formato de fecha: dd/MM/yyyy o dd/MM/yyyy-dd/MM/yyyy para rangos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente", content = @Content(schema = @Schema(implementation = ReporteEstadoCuentaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido o parámetros incorrectos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<ReporteEstadoCuentaDTO> generarReporte(
            @Parameter(description = "Fecha o rango de fechas (formato: dd/MM/yyyy o dd/MM/yyyy-dd/MM/yyyy)", example = "01/02/2022", required = true) @RequestParam String fecha,
            @Parameter(description = "ID del cliente", example = "1", required = true) @RequestParam Long cliente) {
        log.info("GET /reportes - Generating report for cliente {} with fecha {}", cliente, fecha);
        ReporteEstadoCuentaDTO reporte = reporteService.generarReporte(fecha, cliente);
        return ResponseEntity.ok(reporte);
    }
}
