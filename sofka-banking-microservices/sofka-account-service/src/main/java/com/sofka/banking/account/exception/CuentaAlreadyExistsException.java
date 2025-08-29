package com.sofka.banking.account.exception;

public class CuentaAlreadyExistsException extends RuntimeException {

    public static CuentaAlreadyExistsException porNumeroCuenta(String numeroCuenta) {
        return new CuentaAlreadyExistsException("Ya existe una cuenta con número: " + numeroCuenta);
    }

    public CuentaAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
