package com.sofka.banking.account.repository;

import com.sofka.banking.account.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * Buscar movimientos por cuenta en un rango de fechas específico
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
            "AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha ASC")
    List<Movimiento> findMovimientosParaReporte(@Param("numeroCuenta") String numeroCuenta,
                                                @Param("fechaInicio") LocalDateTime fechaInicio,
                                                @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Buscar movimientos por número de cuenta
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.numeroCuenta = :numeroCuenta ORDER BY m.fecha DESC")
    List<Movimiento> findByNumeroCuentaOrderByFechaDesc(@Param("numeroCuenta") String numeroCuenta);

    /**
     * Buscar movimientos por cliente y rango de fechas
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.clienteId = :clienteId " +
            "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteId(@Param("clienteId") String clienteId);


}
