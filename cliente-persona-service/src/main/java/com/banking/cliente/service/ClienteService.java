package com.banking.cliente.service;

import com.banking.cliente.dto.ClienteEventDTO;
import com.banking.cliente.dto.ClienteRequestDTO;
import com.banking.cliente.dto.ClienteResponseDTO;
import com.banking.cliente.exception.ResourceAlreadyExistsException;
import com.banking.cliente.exception.ResourceNotFoundException;
import com.banking.cliente.mapper.ClienteMapper;
import com.banking.cliente.model.Cliente;
import com.banking.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final MessagePublisherService messagePublisher;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public ClienteResponseDTO getClienteById(Long id) {
        log.info("Getting cliente by id: {}", id);
        Cliente cliente = clienteRepository.findByIdWithPersona(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO getClienteByClienteId(String clienteId) {
        log.info("Getting cliente by clienteId: {}", clienteId);
        Cliente cliente = clienteRepository.findByClienteIdWithPersona(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con clienteId: " + clienteId));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> getAllClientes() {
        log.info("Getting all clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO createCliente(ClienteRequestDTO requestDTO) {
        log.info("Creating cliente with clienteId: {}", requestDTO.getClienteId());

        if (clienteRepository.existsByClienteId(requestDTO.getClienteId())) {
            throw new ResourceAlreadyExistsException(
                    "Cliente con clienteId " + requestDTO.getClienteId() + " ya existe");
        }

        Cliente cliente = clienteMapper.toEntity(requestDTO);
        Cliente savedCliente = clienteRepository.save(cliente);

        ClienteEventDTO event = clienteMapper.toEventDTO(savedCliente);
        event.setEventType("CREATED");
        messagePublisher.publishClienteCreated(event);

        return clienteMapper.toResponseDTO(savedCliente);
    }

    public ClienteResponseDTO updateCliente(Long id, ClienteRequestDTO requestDTO) {
        log.info("Updating cliente with id: {}", id);

        Cliente cliente = clienteRepository.findByIdWithPersona(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        clienteMapper.updateEntityFromDto(requestDTO, cliente);
        Cliente updatedCliente = clienteRepository.save(cliente);

        ClienteEventDTO event = clienteMapper.toEventDTO(updatedCliente);
        event.setEventType("UPDATED");
        messagePublisher.publishClienteUpdated(event);

        return clienteMapper.toResponseDTO(updatedCliente);
    }

    public void deleteCliente(Long id) {
        log.info("Deleting cliente with id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        ClienteEventDTO event = clienteMapper.toEventDTO(cliente);
        event.setEventType("DELETED");
        event.setEstado(false);

        clienteRepository.delete(cliente);
        messagePublisher.publishClienteDeleted(event);
    }
}
