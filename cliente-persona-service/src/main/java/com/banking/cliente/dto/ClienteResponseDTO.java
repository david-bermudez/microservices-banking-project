package com.banking.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información del cliente")
public class ClienteResponseDTO {

    @Schema(description = "ID interno del cliente", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Género del cliente", example = "M")
    private String genero;

    @Schema(description = "Edad del cliente", example = "30")
    private Integer edad;

    @Schema(description = "Número de identificación del cliente", example = "1234567890")
    private String identificacion;

    @Schema(description = "Dirección del cliente", example = "Calle Principal 123")
    private String direccion;

    @Schema(description = "Número de teléfono del cliente", example = "+593987654321")
    private String telefono;

    @Schema(description = "Identificador único del cliente", example = "CLI001")
    private String clienteId;

    @Schema(description = "Estado del cliente", example = "true")
    private Boolean estado;

    @Schema(description = "Fecha y hora de creación", example = "2024-01-26T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2024-01-26T15:45:00")
    private LocalDateTime updatedAt;
}
