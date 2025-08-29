package com.sofka.banking.account.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaDto {

    private Long cuentaId;

    @NotBlank(message = "El número de cuenta es requerido")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es requerido")
    @Pattern(regexp = "^(Ahorro|Corriente)$", message = "El tipo de cuenta debe ser 'Ahorro' o 'Corriente'")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;

    private BigDecimal saldoActual;

    private Boolean estado;

    @NotBlank(message = "El ID del cliente es requerido")
    private String clienteId;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructores
    public CuentaDto() {}


    // Getters y Setters
    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "CuentaDto{" +
                "cuentaId=" + cuentaId +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoInicial=" + saldoInicial +
                ", saldoActual=" + saldoActual +
                ", estado=" + estado +
                ", clienteId='" + clienteId + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
