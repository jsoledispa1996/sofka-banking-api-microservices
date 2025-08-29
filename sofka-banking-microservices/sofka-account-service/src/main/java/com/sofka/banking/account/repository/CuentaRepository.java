package com.sofka.banking.account.repository;

import com.sofka.banking.account.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    /**
     * Verificar si existe una cuenta con el número dado
     */
    boolean existsByNumeroCuenta(String numeroCuenta);

    /**
     * Buscar cuenta por número de cuenta
     */
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    /**
     * Buscar cuentas por clienteId
     */
    List<Cuenta> findByClienteId(String clienteId);


}
