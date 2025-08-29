package com.sofka.banking.account.exception;

public class CuentaNotFoundException extends RuntimeException {


    public static CuentaNotFoundException porId(Long id) {
        return new CuentaNotFoundException("Cuenta no encontrada con ID: " + id);
    }

    public static CuentaNotFoundException porNumeroCuenta(String numeroCuenta) {
        return new CuentaNotFoundException("Cuenta no encontrada con número: " + numeroCuenta);
    }

    public static CuentaNotFoundException porCliente(String clienteId) {
        return new CuentaNotFoundException("No se encontraron cuentas para el cliente: " + clienteId);
    }

    public CuentaNotFoundException(String mensaje) {
        super(mensaje);
    }

}
