package com.sofka.banking.client.entity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ba_clientes")
@PrimaryKeyJoinColumn(name = "cl_id_persona")
public class Cliente extends Persona {

    @NotBlank(message = "El clienteId es requerido")
    @Column(name = "cl_id_cliente", nullable = false, unique = true, length = 20)
    private String clienteId;

    @NotBlank(message = "La contraseña es requerida")
    @Column(name = "cl_contrasena", nullable = false, length = 255)
    private String contrasena;

    @NotNull(message = "El estado es requerido")
    @Column(name = "cl_estado", nullable = false)
    private Boolean estado;

    // Constructores
    public Cliente() {
        super();
        this.estado = true; // Por defecto activo
    }

    public Cliente(String nombre, String genero, Integer edad, String identificacion,
                   String direccion, String telefono, String clienteId, String contrasena, Boolean estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado != null ? estado : true;
    }

    // Getters y Setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId='" + clienteId + '\'' +
                ", estado=" + estado +
                ", " + super.toString() +
                '}';
    }
}
