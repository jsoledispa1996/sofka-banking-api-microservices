package com.sofka.banking.account.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    private final BigDecimal saldoActual;
    private final BigDecimal valorSolicitado;

    public SaldoInsuficienteException(String mensaje, BigDecimal saldoActual, BigDecimal valorSolicitado) {
        super(mensaje);
        this.saldoActual = saldoActual;
        this.valorSolicitado = valorSolicitado;
    }

    public SaldoInsuficienteException(BigDecimal saldoActual, BigDecimal valorSolicitado) {
        super(String.format("Saldo insuficiente. Saldo actual: %s, Valor solicitado: %s",
                saldoActual, valorSolicitado));
        this.saldoActual = saldoActual;
        this.valorSolicitado = valorSolicitado;
    }

    public static SaldoInsuficienteException para(BigDecimal saldoActual, BigDecimal valorSolicitado) {
        return new SaldoInsuficienteException(saldoActual, valorSolicitado);
    }

    public static SaldoInsuficienteException conMensaje(String mensaje, BigDecimal saldoActual, BigDecimal valorSolicitado) {
        return new SaldoInsuficienteException(mensaje, saldoActual, valorSolicitado);
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }
}
