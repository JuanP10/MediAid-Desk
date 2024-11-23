package org.example.mediaid.modelos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Enfermero {
    private String nombre;
    private String cedula;

    // Constructor sin parámetros
    public Enfermero() {}

    // Constructor con parámetros
    @JsonCreator
    public Enfermero(@JsonProperty("nombre") String nombre, @JsonProperty("especialidad") String especialidad) {
        this.nombre = nombre;
        this.cedula = especialidad;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}

