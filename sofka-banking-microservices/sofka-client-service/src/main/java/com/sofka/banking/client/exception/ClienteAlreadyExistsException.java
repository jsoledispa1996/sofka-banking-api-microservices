package com.sofka.banking.client.exception;

public class ClienteAlreadyExistsException extends RuntimeException{
    public static ClienteAlreadyExistsException porClienteId(String clienteId) {
        return new ClienteAlreadyExistsException("Ya existe un cliente con ClienteId: " + clienteId);
    }

    public static ClienteAlreadyExistsException porIdentificacion(String identificacion) {
        return new ClienteAlreadyExistsException("Ya existe una persona con identificación: " + identificacion);
    }
    public ClienteAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    public ClienteAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
