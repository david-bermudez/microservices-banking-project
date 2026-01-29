package com.banking.cliente;

import com.banking.cliente.dto.ClienteRequestDTO;
import com.banking.cliente.dto.ClienteResponseDTO;
import com.banking.cliente.dto.ClienteEventDTO;
import com.banking.cliente.exception.ResourceAlreadyExistsException;
import com.banking.cliente.exception.ResourceNotFoundException;
import com.banking.cliente.mapper.ClienteMapper;
import com.banking.cliente.model.Cliente;
import com.banking.cliente.model.Persona;
import com.banking.cliente.repository.ClienteRepository;
import com.banking.cliente.service.ClienteService;
import com.banking.cliente.service.MessagePublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MessagePublisherService messagePublisher;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteRequestDTO requestDTO;
    private ClienteResponseDTO responseDTO;
    private Cliente cliente;
    private Persona persona;
    private ClienteEventDTO clienteEventDTO;

    @BeforeEach
    void setUp() {
        requestDTO = ClienteRequestDTO.builder()
                .nombre("Jose Lema")
                .genero("M")
                .edad(35)
                .identificacion("098254785")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .clienteId("1234")
                .contrasena("1234")
                .estado(true)
                .build();

        persona = Persona.builder()
                .id(1L)
                .nombre("Jose Lema")
                .genero("M")
                .edad(35)
                .identificacion("098254785")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .build();

        cliente = Cliente.builder()
                .id(1L)
                .clienteId("1234")
                .contrasena("1234")
                .estado(true)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        clienteEventDTO = ClienteEventDTO.builder()
                .clienteDbId(1L)
                .clienteId("1234")
                .nombre("Jose Lema")
                .estado(true)
                .eventType("CREATED")
                .build();

        responseDTO = ClienteResponseDTO.builder()
                .id(1L)
                .clienteId("1234")
                .nombre("Jose Lema")
                .estado(true)
                .build();

        cliente.setPersona(persona);
    }

    @Test
    void createCliente_Success() {
        // Given
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toEventDTO(any(Cliente.class))).thenReturn(clienteEventDTO);
        when(clienteMapper.toEntity(any(ClienteRequestDTO.class))).thenReturn(cliente);
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(responseDTO);
        // When
        ClienteResponseDTO response = clienteService.createCliente(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals("Jose Lema", response.getNombre());
        assertEquals("1234", response.getClienteId());
        assertTrue(response.getEstado());

        verify(clienteRepository).existsByClienteId("1234");
        verify(clienteRepository).save(any(Cliente.class));
        verify(messagePublisher).publishClienteCreated(any());
    }

    @Test
    void createCliente_AlreadyExists_ThrowsException() {
        // Given
        when(clienteRepository.existsByClienteId(anyString())).thenReturn(true);

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            clienteService.createCliente(requestDTO);
        });

        verify(clienteRepository).existsByClienteId("1234");
        verify(clienteRepository, never()).save(any());
        verify(messagePublisher, never()).publishClienteCreated(any());
    }

    @Test
    void getClienteById_Success() {
        // Given
        when(clienteRepository.findByIdWithPersona(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(responseDTO);
        // When
        ClienteResponseDTO response = clienteService.getClienteById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Jose Lema", response.getNombre());

        verify(clienteRepository).findByIdWithPersona(1L);
    }

    @Test
    void getClienteById_NotFound_ThrowsException() {
        // Given
        when(clienteRepository.findByIdWithPersona(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.getClienteById(999L);
        });

        verify(clienteRepository).findByIdWithPersona(999L);
    }

    @Test
    void getAllClientes_Success() {

        // Given
        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .clienteId("5678")
                .contrasena("5678")
                .estado(true)
                .build();

        Persona persona2 = Persona.builder()
                .id(2L)
                .nombre("Marianela Montalvo")
                .genero("F")
                .edad(28)
                .identificacion("097548965")
                .build();

        cliente2.setPersona(persona2);

        ClienteResponseDTO dto1 = new ClienteResponseDTO();
        dto1.setNombre("Jose Lema");

        ClienteResponseDTO dto2 = new ClienteResponseDTO();
        dto2.setNombre("Marianela Montalvo");

        when(clienteRepository.findAll())
                .thenReturn(Arrays.asList(cliente, cliente2));

        when(clienteMapper.toResponseDTO(cliente))
                .thenReturn(dto1);

        when(clienteMapper.toResponseDTO(cliente2))
                .thenReturn(dto2);

        // When
        List<ClienteResponseDTO> response = clienteService.getAllClientes();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Jose Lema", response.get(0).getNombre());
        assertEquals("Marianela Montalvo", response.get(1).getNombre());

        verify(clienteRepository).findAll();
    }

    @Test
    void updateCliente_Success() {
        // Given
        when(clienteRepository.findByIdWithPersona(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toEventDTO(any(Cliente.class))).thenReturn(clienteEventDTO);
        when(clienteMapper.toResponseDTO(any(Cliente.class))).thenReturn(responseDTO);

        ClienteRequestDTO updateDTO = ClienteRequestDTO.builder()
                .nombre("Jose Lema Updated")
                .genero("M")
                .edad(36)
                .identificacion("098254785")
                .direccion("Nueva Direccion")
                .telefono("098254785")
                .clienteId("1234")
                .contrasena("newpass")
                .estado(true)
                .build();

        // When
        ClienteResponseDTO response = clienteService.updateCliente(1L, updateDTO);

        // Then
        assertNotNull(response);
        verify(clienteRepository).findByIdWithPersona(1L);
        verify(clienteRepository).save(any(Cliente.class));
        verify(messagePublisher).publishClienteUpdated(any());
    }

    @Test
    void deleteCliente_Success() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));
        when(clienteMapper.toEventDTO(cliente)).thenReturn(clienteEventDTO);
        // When
        clienteService.deleteCliente(1L);
        // Then
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).delete(cliente);
        verify(messagePublisher).publishClienteDeleted(any());
    }

    @Test
    void deleteCliente_NotFound_ThrowsException() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.deleteCliente(999L);
        });

        verify(clienteRepository).findById(999L);
        verify(clienteRepository, never()).delete(any());
    }
}
