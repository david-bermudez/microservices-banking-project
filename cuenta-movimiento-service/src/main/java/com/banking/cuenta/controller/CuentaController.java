package com.banking.cuenta.controller;

import com.banking.cuenta.dto.CuentaRequestDTO;
import com.banking.cuenta.dto.CuentaResponseDTO;
import com.banking.cuenta.service.CuentaService;
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
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cuentas", description = "API para la gestión de cuentas bancarias")
public class CuentaController {

    private final CuentaService cuentaService;

    @Operation(summary = "Crear una nueva cuenta", description = "Crea una nueva cuenta bancaria para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente", content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaRequestDTO requestDTO) {
        log.info("POST /cuentas - Creating cuenta");
        CuentaResponseDTO response = cuentaService.createCuenta(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cuenta por ID", description = "Recupera la información de una cuenta específica usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada", content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuentaById(
            @Parameter(description = "ID de la cuenta", required = true) @PathVariable Long id) {
        log.info("GET /cuentas/{} - Getting cuenta by id", id);
        CuentaResponseDTO response = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todas las cuentas", description = "Recupera una lista de todas las cuentas. Opcionalmente se puede filtrar por clienteId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuentas recuperada exitosamente", content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> getAllCuentas(
            @Parameter(description = "ID del cliente para filtrar cuentas") @RequestParam(required = false) Long clienteId) {
        log.info("GET /cuentas - Getting all cuentas");

        if (clienteId != null) {
            List<CuentaResponseDTO> response = cuentaService.getCuentasByClienteId(clienteId);
            return ResponseEntity.ok(response);
        }

        List<CuentaResponseDTO> response = cuentaService.getAllCuentas();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar una cuenta", description = "Actualiza la información de una cuenta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente", content = @Content(schema = @Schema(implementation = CuentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> updateCuenta(
            @Parameter(description = "ID de la cuenta", required = true) @PathVariable Long id,
            @Valid @RequestBody CuentaRequestDTO requestDTO) {
        log.info("PUT /cuentas/{} - Updating cuenta", id);
        CuentaResponseDTO response = cuentaService.updateCuenta(id, requestDTO);
        return ResponseEntity.ok(response);
    }
}
