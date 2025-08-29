package com.sofka.banking.client.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class ClienteDto {

    private Long personaId;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @Pattern(regexp = "^(Masculino|Femenino)$", message = "El género debe ser M, F, Masculino o Femenino")
    private String genero;

    @NotNull(message = "La edad es requerida")
    @Positive(message = "La edad debe ser un número positivo")
    private Integer edad;

    @NotBlank(message = "La identificación es requerida")
    private String identificacion;

    @NotBlank(message = "La dirección es requerida")
    private String direccion;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe contener entre 9 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "El clienteId es requerido")
    private String clienteId;

    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    private Boolean estado;

    // Constructores
    public ClienteDto() {}

    public ClienteDto(String nombre, String genero, Integer edad, String identificacion,
                      String direccion, String telefono, String clienteId, String contrasena, Boolean estado) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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
        return "ClienteDto{" +
                "personaId=" + personaId +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", identificacion='" + identificacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", clienteId='" + clienteId + '\'' +
                ", estado=" + estado +
                '}';
    }
}
