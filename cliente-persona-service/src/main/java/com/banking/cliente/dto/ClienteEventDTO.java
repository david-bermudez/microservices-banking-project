package com.banking.cliente.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEventDTO {

    private Long clienteDbId;
    private String clienteId;
    private String nombre;
    private Boolean estado;
    private String eventType; // CREATED, UPDATED, DELETED
}
