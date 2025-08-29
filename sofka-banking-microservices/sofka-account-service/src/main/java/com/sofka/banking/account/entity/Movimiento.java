package com.sofka.banking.account.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ba_movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mo_id_movimiento")
    private Long movimientoId;

    @NotNull(message = "La fecha es requerida")
    @Column(name = "mo_fecha", nullable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento es requerido")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser 'DEPOSITO' o 'RETIRO'")
    @Column(name = "mo_tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    @Column(name = "mo_valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "El saldo es requerido")
    @Column(name = "mo_saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "mo_descripcion", length = 255)
    private String descripcion;

    // Relación muchos a uno con cuenta por ID interno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_id_cuenta", referencedColumnName = "cu_id_cuenta", nullable = false)
    private Cuenta cuenta;

    // Constructores
    public Movimiento() {
        this.fecha = LocalDateTime.now();
    }

    public Movimiento(String tipoMovimiento, BigDecimal valor, BigDecimal saldo, Cuenta cuenta) {
        this();
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuenta = cuenta;
    }

    // Getters y Setters
    public Long getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Long movimientoId) {
        this.movimientoId = movimientoId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

}
