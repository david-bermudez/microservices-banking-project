package com.banking.cuenta.controller;

import com.banking.cuenta.dto.MovimientoRequestDTO;
import com.banking.cuenta.dto.MovimientoResponseDTO;
import com.banking.cuenta.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Movimientos", description = "API para la gestión de movimientos bancarios")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Registrar un nuevo movimiento", description = "Registra un nuevo movimiento bancario (débito o crédito) en una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente", content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o saldo insuficiente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoRequestDTO requestDTO) {
        log.info("POST /movimientos - Registrando movimiento");
        MovimientoResponseDTO response = movimientoService.registrarMovimiento(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener movimiento por ID", description = "Recupera la información de un movimiento específico usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado", content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> getMovimientoById(
            @Parameter(description = "ID del movimiento", required = true) @PathVariable Long id) {
        log.info("GET /movimientos/{} - Getting movimiento by id", id);
        MovimientoResponseDTO response = movimientoService.getMovimientoById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los movimientos", description = "Recupera una lista de todos los movimientos. Opcionalmente se puede filtrar por cuentaId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos recuperada exitosamente", content = @Content(schema = @Schema(implementation = MovimientoResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> getAllMovimientos(
            @Parameter(description = "ID de la cuenta para filtrar movimientos") @RequestParam(required = false) Long cuentaId) {
        log.info("GET /movimientos - Getting all movimientos");

        if (cuentaId != null) {
            List<MovimientoResponseDTO> response = movimientoService.getMovimientosByCuentaId(cuentaId);
            return ResponseEntity.ok(response);
        }

        List<MovimientoResponseDTO> response = movimientoService.getAllMovimientos();
        return ResponseEntity.ok(response);
    }
}
