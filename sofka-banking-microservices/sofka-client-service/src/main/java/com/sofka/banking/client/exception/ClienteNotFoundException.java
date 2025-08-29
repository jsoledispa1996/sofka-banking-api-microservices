package com.sofka.banking.client.exception;

public class ClienteNotFoundException extends RuntimeException {

    public static ClienteNotFoundException porId(Long id) {
        return new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
    }

    public static ClienteNotFoundException porClienteId(String clienteId) {
        return new ClienteNotFoundException("Cliente no encontrado con ClienteId: " + clienteId);
    }

    public static ClienteNotFoundException porIdentificacion(String identificacion) {
        return new ClienteNotFoundException("Cliente no encontrado con identificación: " + identificacion);
    }

    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}
