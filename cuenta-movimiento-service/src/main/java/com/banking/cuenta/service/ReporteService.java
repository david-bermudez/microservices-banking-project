package com.banking.cuenta.service;

import com.banking.cuenta.dto.CuentaReporteDTO;
import com.banking.cuenta.dto.MovimientoReporteDTO;
import com.banking.cuenta.dto.ReporteEstadoCuentaDTO;
import com.banking.cuenta.model.Cuenta;
import com.banking.cuenta.model.Movimiento;
import com.banking.cuenta.repository.CuentaRepository;
import com.banking.cuenta.repository.MovimientoRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReporteService {

        private final CuentaRepository cuentaRepository;
        private final MovimientoRepository movimientoRepository;

        public ReporteEstadoCuentaDTO generarReporte(String fechaStr, Long clienteId) {
                log.info("Generando reporte para cliente: {} con fecha: {}", clienteId, fechaStr);

                String[] fechas = fechaStr.split("-");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime fechaInicio;
                LocalDateTime fechaFin;

                if (fechas.length == 2) {
                        fechaInicio = LocalDateTime.parse(fechas[0].trim() + " 00:00:00",
                                        formatter);
                        fechaFin = LocalDateTime.parse(fechas[1].trim() + " 23:59:59",
                                        formatter);
                } else {
                        fechaInicio = LocalDateTime.parse(fechaStr.trim() + " 00:00:00",
                                        formatter);
                        fechaFin = fechaInicio.plusDays(1).minusSeconds(1);
                }
                List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

                if (cuentas.isEmpty()) {
                        log.warn("No se encontraron cuentas para el cliente: {}", clienteId);
                }

                // Obtain the list of account ids
                List<Long> cuentaIds = cuentas.stream()
                                .map(Cuenta::getId)
                                .collect(Collectors.toList());

                // Obtain the list of movements given de Ids of accounts and the date range
                List<Movimiento> movimientos = movimientoRepository.findByCuentaIdsAndFechaBetween(
                                cuentaIds, fechaInicio, fechaFin);

                // Group the movements by account id
                Map<Long, List<Movimiento>> movimientosPorCuenta = movimientos.stream()
                                .collect(Collectors.groupingBy(m -> m.getCuenta().getId()));

                // for every account, we need to check the movements and create a report DTO
                List<CuentaReporteDTO> cuentasReporte = cuentas.stream().map(
                                (Cuenta cuenta) -> {
                                        List<Movimiento> movimientosCuenta = movimientosPorCuenta.getOrDefault(
                                                        cuenta.getId(), new ArrayList<>());

                                        List<MovimientoReporteDTO> movimientosReporte = movimientosCuenta.stream()
                                                        .map(movimiento -> MovimientoReporteDTO.builder()
                                                                        .fecha(movimiento.getFecha())
                                                                        .tipoMovimiento(movimiento.getTipoMovimiento())
                                                                        .valor(movimiento.getValor())
                                                                        .saldoAnterior(movimiento.getSaldoAnterior())
                                                                        .saldoNuevo(movimiento.getSaldoNuevo())
                                                                        .descripcion(movimiento.getDescripcion())
                                                                        .build())
                                                        .collect(Collectors.toList());

                                        return CuentaReporteDTO.builder()
                                                        .numeroCuenta(cuenta.getNumeroCuenta())
                                                        .saldoInicial(cuenta.getSaldoInicial())
                                                        .movimientos(movimientosReporte)
                                                        .saldoActual(cuenta.getSaldoActual())
                                                        .build();
                                }).collect(Collectors.toList());

                return ReporteEstadoCuentaDTO.builder()
                                .cliente("Cliente " + clienteId)
                                .identificacion("N/A")
                                .fechaInicio(fechaInicio)
                                .fechaFin(fechaFin)
                                .cuentas(cuentasReporte)
                                .build();
        }
}
