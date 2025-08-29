package com.sofka.banking.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReporteDto {
    private String clienteId;
    private String nombreCliente;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private List<CuentaReporteDto> cuentas;

    // Constructores
    public ReporteDto() {}

    public ReporteDto(String clienteId, String nombreCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<CuentaReporteDto> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaReporteDto> cuentas) {
        this.cuentas = cuentas;
    }


    // Clase interna para información de cuenta en el reporte
    public static class CuentaReporteDto {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoInicial;
        private BigDecimal saldoActual;
        private Boolean estado;
        private List<MovimientoReporteDto> movimientos;

        // Constructores
        public CuentaReporteDto() {}

        public CuentaReporteDto(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial,
                                BigDecimal saldoActual, Boolean estado) {
            this.numeroCuenta = numeroCuenta;
            this.tipoCuenta = tipoCuenta;
            this.saldoInicial = saldoInicial;
            this.saldoActual = saldoActual;
            this.estado = estado;
        }

        // Getters y Setters
        public String getNumeroCuenta() { return numeroCuenta; }
        public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

        public String getTipoCuenta() { return tipoCuenta; }
        public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

        public BigDecimal getSaldoInicial() { return saldoInicial; }
        public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

        public BigDecimal getSaldoActual() { return saldoActual; }
        public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }

        public Boolean getEstado() { return estado; }
        public void setEstado(Boolean estado) { this.estado = estado; }

        public List<MovimientoReporteDto> getMovimientos() { return movimientos; }
        public void setMovimientos(List<MovimientoReporteDto> movimientos) { this.movimientos = movimientos; }
    }

    // Clase interna para información de movimiento en el reporte
    public static class MovimientoReporteDto {
        private LocalDateTime fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldo;
        private String descripcion;

        // Constructores
        public MovimientoReporteDto() {}

        public MovimientoReporteDto(LocalDateTime fecha, String tipoMovimiento, BigDecimal valor,
                                    BigDecimal saldo, String descripcion) {
            this.fecha = fecha;
            this.tipoMovimiento = tipoMovimiento;
            this.valor = valor;
            this.saldo = saldo;
            this.descripcion = descripcion;
        }

        // Getters y Setters
        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

        public String getTipoMovimiento() { return tipoMovimiento; }
        public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }

        public BigDecimal getSaldo() { return saldo; }
        public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    @Override
    public String toString() {
        return "ReporteDto{" +
                "clienteId='" + clienteId + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", cuentas=" + (cuentas != null ? cuentas.size() : 0) +
                '}';
    }
}
