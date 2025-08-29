package com.sofka.banking.account.exception;

public class ClienteNotFoundException extends RuntimeException {

    public static ClienteNotFoundException porClienteId(String clienteId) {
        return new ClienteNotFoundException("Cliente no encontrado con ID: " + clienteId);
    }


    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}
