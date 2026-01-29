package com.banking.cuenta.service;

import com.banking.cuenta.dto.CuentaRequestDTO;
import com.banking.cuenta.dto.CuentaResponseDTO;
import com.banking.cuenta.exception.ResourceNotFoundException;
import com.banking.cuenta.mapper.CuentaMapper;
import com.banking.cuenta.model.Cuenta;
import com.banking.cuenta.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Transactional(readOnly = false)
    public CuentaResponseDTO createCuenta(CuentaRequestDTO requestDTO) {
        log.info("Creating cuenta with numeroCuenta: {}", requestDTO.getNumeroCuenta());

        Cuenta cuenta = cuentaMapper.toEntity(requestDTO);

        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(savedCuenta);
    }

    public CuentaResponseDTO getCuentaById(Long id) {
        log.info("Getting cuenta by id: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    public CuentaResponseDTO getCuentaByNumeroCuenta(String numeroCuenta) {
        log.info("Getting cuenta by numeroCuenta: {}", numeroCuenta);
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    public List<CuentaResponseDTO> getAllCuentas() {
        log.info("Getting all cuentas");
        return cuentaMapper.toResponseDTOList(cuentaRepository.findAll());
    }

    public List<CuentaResponseDTO> getCuentasByClienteId(Long clienteId) {
        log.info("Getting cuentas by clienteId: {}", clienteId);
        return cuentaMapper.toResponseDTOList(cuentaRepository.findByClienteId(clienteId));
    }

    @Transactional(readOnly = false)
    public CuentaResponseDTO updateCuenta(Long id, CuentaRequestDTO requestDTO) {
        log.info("Updating cuenta with id: {}", id);

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));

        cuentaMapper.updateEntityFromDto(requestDTO, cuenta);

        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }
}
