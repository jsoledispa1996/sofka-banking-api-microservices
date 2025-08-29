package com.sofka.banking.client.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "ba_personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pe_id_persona")
    private Long personaId;

    @NotBlank(message = "El nombre es requerido")
    @Column(name = "pe_nombre", nullable = false, length = 255)
    private String nombre;

    @NotBlank(message = "El género es requerido")
    @Pattern(regexp = "^(Masculino|Femenino)$", message = "El género debe ser Masculino o Femenino")
    @Column(name = "pe_genero", nullable = false, length = 10)
    private String genero;

    @NotNull(message = "La edad es requerida")
    @Positive(message = "La edad debe ser un número positivo")
    @Column(name = "pe_edad", nullable = false)
    private Integer edad;

    @NotBlank(message = "La identificación es requerida")
    @Column(name = "pe_identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @NotBlank(message = "La dirección es requerida")
    @Column(name = "pe_direccion", nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "El teléfono debe contener entre 9 y 15 dígitos")
    @Column(name = "pe_telefono", nullable = false, length = 15)
    private String telefono;

    // Constructores
    public Persona() {}

    public Persona(String nombre, String genero, Integer edad, String identificacion, String direccion, String telefono) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
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

    @Override
    public String toString() {
        return "Persona{" +
                "personaId=" + personaId +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", identificacion='" + identificacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
