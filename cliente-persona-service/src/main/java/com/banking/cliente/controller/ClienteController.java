package com.banking.cliente.controller;

import com.banking.cliente.dto.ClienteRequestDTO;
import com.banking.cliente.dto.ClienteResponseDTO;
import com.banking.cliente.service.ClienteService;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clientes", description = "API para la gestión de clientes y personas")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente en el sistema con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("POST /clientes - Creating cliente");
        ClienteResponseDTO response = clienteService.createCliente(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cliente por ID", description = "Recupera la información de un cliente específico usando su ID interno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(
            @Parameter(description = "ID interno del cliente", required = true) @PathVariable Long id) {
        log.info("GET /clientes/{} - Getting cliente by id", id);
        ClienteResponseDTO response = clienteService.getClienteById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cliente por clienteId", description = "Recupera la información de un cliente usando su identificador único de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-cliente-id/{clienteId}")
    public ResponseEntity<ClienteResponseDTO> getClienteByClienteId(
            @Parameter(description = "Identificador único del cliente", required = true) @PathVariable String clienteId) {
        log.info("GET /clientes/by-cliente-id/{} - Getting cliente by clienteId", clienteId);
        ClienteResponseDTO response = clienteService.getClienteByClienteId(clienteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los clientes", description = "Recupera una lista de todos los clientes registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada exitosamente", content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        log.info("GET /clientes - Getting all clientes");
        List<ClienteResponseDTO> response = clienteService.getAllClientes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar un cliente", description = "Actualiza la información de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @Parameter(description = "ID interno del cliente", required = true) @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("PUT /clientes/{} - Updating cliente", id);
        ClienteResponseDTO response = clienteService.updateCliente(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente del sistema usando su ID interno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "ID interno del cliente", required = true) @PathVariable Long id) {
        log.info("DELETE /clientes/{} - Deleting cliente", id);
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}
