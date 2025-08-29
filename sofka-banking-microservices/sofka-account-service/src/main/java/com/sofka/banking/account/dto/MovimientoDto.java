package com.sofka.banking.account.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoDto {

    private Long movimientoId;
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento es requerido")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser 'DEPOSITO' o 'RETIRO'")
    private String tipoMovimiento;

    @NotNull(message = "El valor es requerido")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    private BigDecimal saldo;
    private String descripcion;

    // Para identificar la cuenta
    @NotBlank(message = "El número de cuenta es requerido")
    private String numeroCuenta;

    private Long cuentaId;

    // Constructores
    public MovimientoDto() {}

    public MovimientoDto(String tipoMovimiento, BigDecimal valor, String numeroCuenta) {
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.numeroCuenta = numeroCuenta;
    }

    public MovimientoDto(String tipoMovimiento, BigDecimal valor, String descripcion, String numeroCuenta) {
        this(tipoMovimiento, valor, numeroCuenta);
        this.descripcion = descripcion;
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

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    @Override
    public String toString() {
        return "MovimientoDto{" +
                "movimientoId=" + movimientoId +
                ", fecha=" + fecha +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", descripcion='" + descripcion + '\'' +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                '}';
    }
}
