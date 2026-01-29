package com.banking.cuenta.service;

import com.banking.cuenta.dto.MovimientoRequestDTO;
import com.banking.cuenta.dto.MovimientoResponseDTO;
import com.banking.cuenta.exception.InsufficientBalanceException;
import com.banking.cuenta.exception.ResourceNotFoundException;
import com.banking.cuenta.mapper.MovimientoMapper;
import com.banking.cuenta.model.Cuenta;
import com.banking.cuenta.model.Movimiento;
import com.banking.cuenta.repository.CuentaRepository;
import com.banking.cuenta.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO requestDTO) {
        log.info("Registrando movimiento para cuenta: {}", requestDTO.getCuentaId());

        Cuenta cuenta = cuentaRepository.findById(requestDTO.getCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada con id: " + requestDTO.getCuentaId()));

        BigDecimal saldoAnterior = cuenta.getSaldoActual();
        BigDecimal valor = requestDTO.getValor();
        BigDecimal saldoNuevo = saldoAnterior.add(valor);

        if (saldoNuevo.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        Movimiento movimiento = movimientoMapper.toEntity(requestDTO);
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldoAnterior(saldoAnterior);
        movimiento.setSaldoNuevo(saldoNuevo);

        cuenta.setSaldoActual(saldoNuevo);
        cuentaRepository.save(cuenta);

        Movimiento savedMovimiento = movimientoRepository.save(movimiento);

        log.info("Movimiento registrado exitosamente. Saldo anterior: {}, Valor: {}, Saldo nuevo: {}",
                saldoAnterior, valor, saldoNuevo);

        return movimientoMapper.toResponseDTO(savedMovimiento);
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO getMovimientoById(Long id) {
        log.info("Getting movimiento by id: {}", id);
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
        return movimientoMapper.toResponseDTO(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> getAllMovimientos() {
        log.info("Getting all movimientos");
        return movimientoMapper.toResponseDTOList(movimientoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> getMovimientosByCuentaId(Long cuentaId) {
        log.info("Getting movimientos by cuentaId: {}", cuentaId);
        return movimientoMapper.toResponseDTOList(
                movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId));
    }
}
