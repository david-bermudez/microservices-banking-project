package com.banking.cliente.mapper;

import com.banking.cliente.dto.ClienteEventDTO;
import com.banking.cliente.dto.ClienteRequestDTO;
import com.banking.cliente.dto.ClienteResponseDTO;
import com.banking.cliente.model.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "nombre", target = "persona.nombre")
    @Mapping(source = "genero", target = "persona.genero")
    @Mapping(source = "edad", target = "persona.edad")
    @Mapping(source = "identificacion", target = "persona.identificacion")
    @Mapping(source = "direccion", target = "persona.direccion")
    @Mapping(source = "telefono", target = "persona.telefono")
    @Mapping(source = "clienteId", target = "clienteId")
    @Mapping(source = "contrasena", target = "contrasena")
    @Mapping(source = "estado", target = "estado")
    Cliente toEntity(ClienteRequestDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "persona.nombre", target = "nombre")
    @Mapping(source = "persona.genero", target = "genero")
    @Mapping(source = "persona.edad", target = "edad")
    @Mapping(source = "persona.identificacion", target = "identificacion")
    @Mapping(source = "persona.direccion", target = "direccion")
    @Mapping(source = "persona.telefono", target = "telefono")
    @Mapping(source = "clienteId", target = "clienteId")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ClienteResponseDTO toResponseDTO(Cliente entity);

    @Mapping(source = "id", target = "clienteDbId")
    @Mapping(source = "clienteId", target = "clienteId")
    @Mapping(source = "persona.nombre", target = "nombre")
    @Mapping(source = "estado", target = "estado")
    @Mapping(target = "eventType", ignore = true)
    ClienteEventDTO toEventDTO(Cliente entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "nombre", target = "persona.nombre")
    @Mapping(source = "genero", target = "persona.genero")
    @Mapping(source = "edad", target = "persona.edad")
    @Mapping(source = "direccion", target = "persona.direccion")
    @Mapping(source = "telefono", target = "persona.telefono")
    void updateEntityFromDto(ClienteRequestDTO dto, @MappingTarget Cliente entity);

    @AfterMapping
    default void linkPersona(@MappingTarget Cliente cliente) {
        if (cliente.getPersona() != null) {
            cliente.getPersona().setCliente(cliente);
        }
    }
}
