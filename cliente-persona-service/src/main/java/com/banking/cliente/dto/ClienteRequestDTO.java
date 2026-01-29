package com.banking.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear o actualizar un cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Schema(description = "Género del cliente (M=Masculino, F=Femenino, O=Otro)", example = "M", allowableValues = {
            "M", "F", "O" })
    @Pattern(regexp = "^[MFO]$", message = "El género debe ser M, F u O")
    private String genero;

    @Schema(description = "Edad del cliente", example = "30", minimum = "0", maximum = "150")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    @Max(value = 150, message = "La edad debe ser menor o igual a 150")
    private Integer edad;

    @Schema(description = "Número de identificación del cliente", example = "1234567890")
    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "La identificación no puede exceder 20 caracteres")
    private String identificacion;

    @Schema(description = "Dirección del cliente", example = "Calle Principal 123")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;

    @Schema(description = "Número de teléfono del cliente", example = "+593987654321")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @Schema(description = "Identificador único del cliente en el sistema", example = "CLI001")
    @NotBlank(message = "El clienteId es obligatorio")
    @Size(max = 50, message = "El clienteId no puede exceder 50 caracteres")
    private String clienteId;

    @Schema(description = "Contraseña del cliente", example = "password123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String contrasena;

    @Schema(description = "Estado del cliente (activo/inactivo)", example = "true", defaultValue = "true")
    @Builder.Default
    private Boolean estado = true;
}
