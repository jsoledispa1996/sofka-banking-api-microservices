package com.sofka.banking.client.repository;

import com.sofka.banking.client.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Verificar si existe un cliente con el clienteId dado
     */
    boolean existsByClienteId(String clienteId);

    /**
     * Verificar si existe una persona con la identificación dada
     */
    boolean existsByIdentificacion(String identificacion);

    /**
     * Buscar cliente por clienteId
     */
    Optional<Cliente> findByClienteId(String clienteId);

}
