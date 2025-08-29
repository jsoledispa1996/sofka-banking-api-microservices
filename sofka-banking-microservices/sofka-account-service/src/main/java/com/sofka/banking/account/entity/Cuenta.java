package com.sofka.banking.account.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ba_cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id_cuenta")
    private Long cuentaId;

    @NotBlank(message = "El número de cuenta es requerido")
    @Column(name = "cu_numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es requerido")
    @Pattern(regexp = "^(Ahorro|Corriente)$", message = "El tipo de cuenta debe ser 'Ahorro' o 'Corriente'")
    @Column(name = "cu_tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    @Column(name = "cu_saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull(message = "El saldo actual es requerido")
    @Column(name = "cu_saldo_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;

    @NotNull(message = "El estado es requerido")
    @Column(name = "cu_estado", nullable = false)
    private Boolean estado;

    @NotBlank(message = "El ID del cliente es requerido")
    @Column(name = "cu_id_cliente", nullable = false, length = 20)
    private String clienteId;

    @Column(name = "cu_fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "cu_fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación uno a muchos con movimientos
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();

    public void actualizarSaldo(BigDecimal nuevoSaldo) {
        this.saldoActual = nuevoSaldo;
        this.fechaActualizacion = LocalDateTime.now();
    }

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

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
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
