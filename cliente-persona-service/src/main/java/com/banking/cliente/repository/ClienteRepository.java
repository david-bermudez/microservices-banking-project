package com.banking.cliente.repository;

import com.banking.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(String clienteId);

    boolean existsByClienteId(String clienteId);

    @Query("SELECT c FROM Cliente c JOIN FETCH c.persona WHERE c.id = :id")
    Optional<Cliente> findByIdWithPersona(Long id);

    @Query("SELECT c FROM Cliente c JOIN FETCH c.persona WHERE c.clienteId = :clienteId")
    Optional<Cliente> findByClienteIdWithPersona(String clienteId);

}
