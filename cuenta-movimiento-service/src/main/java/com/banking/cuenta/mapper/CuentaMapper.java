package com.banking.cuenta.mapper;

import com.banking.cuenta.dto.CuentaRequestDTO;
import com.banking.cuenta.dto.CuentaResponseDTO;
import com.banking.cuenta.model.Cuenta;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CuentaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "saldoActual", source = "saldoInicial")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    Cuenta toEntity(CuentaRequestDTO dto);

    @Mapping(source = "numeroCuenta", target = "numeroCuenta")
    @Mapping(source = "tipoCuenta", target = "tipoCuenta")
    @Mapping(source = "saldoInicial", target = "saldoInicial")
    @Mapping(source = "saldoActual", target = "saldoActual")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "clienteId", target = "clienteId")
    CuentaResponseDTO toResponseDTO(Cuenta entity);

    List<CuentaResponseDTO> toResponseDTOList(List<Cuenta> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroCuenta", ignore = true)
    @Mapping(target = "saldoInicial", ignore = true)
    @Mapping(target = "saldoActual", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(source = "tipoCuenta", target = "tipoCuenta")
    @Mapping(source = "estado", target = "estado")
    void updateEntityFromDto(CuentaRequestDTO dto, @MappingTarget Cuenta entity);
}
