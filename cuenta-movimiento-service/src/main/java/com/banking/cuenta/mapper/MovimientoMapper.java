package com.banking.cuenta.mapper;

import com.banking.cuenta.dto.MovimientoRequestDTO;
import com.banking.cuenta.dto.MovimientoResponseDTO;
import com.banking.cuenta.model.Movimiento;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct Mapper para conversión entre entidad Movimiento y DTOs
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MovimientoMapper {

    /**
     * Convierte MovimientoRequestDTO a entidad Movimiento
     * Nota: La cuenta, fecha y saldos se establecen en el servicio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuenta", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "saldoAnterior", ignore = true)
    @Mapping(target = "saldoNuevo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "tipoMovimiento", target = "tipoMovimiento")
    @Mapping(source = "valor", target = "valor")
    @Mapping(source = "descripcion", target = "descripcion")
    Movimiento toEntity(MovimientoRequestDTO dto);

    /**
     * Convierte entidad Movimiento a MovimientoResponseDTO
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "cuenta.id", target = "cuentaId")
    @Mapping(source = "cuenta.numeroCuenta", target = "numeroCuenta")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "tipoMovimiento", target = "tipoMovimiento")
    @Mapping(source = "valor", target = "valor")
    @Mapping(source = "saldoAnterior", target = "saldoAnterior")
    @Mapping(source = "saldoNuevo", target = "saldoNuevo")
    @Mapping(source = "descripcion", target = "descripcion")
    MovimientoResponseDTO toResponseDTO(Movimiento entity);

    /**
     * Convierte lista de entidades a lista de DTOs
     */
    List<MovimientoResponseDTO> toResponseDTOList(List<Movimiento> entities);
}
