package org.example.mediaid.modelos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente {

    private String nombre;
    private String cedula;
    private LocalDate fechaNacimiento;
    private List<Condicion> condicion = new ArrayList<>();
    private boolean esPensionado;

    // Constructor
    @JsonCreator
    public Paciente(@JsonProperty("nombre") String nombre,
                    @JsonProperty("cedula") String cedula,
                    @JsonProperty("fechaNacimiento") LocalDate fechaNacimiento,
                    @JsonProperty("condicion") List<Condicion> condicion,
                    @JsonProperty("esPensionado") boolean esPensionado) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.condicion = condicion;
        this.esPensionado = esPensionado;
    }

    // Getters y setters
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // Cambiar el tipo de retorno y el parámetro de "condicion" para que sea List<Condicion>
    public List<Condicion> getCondicion() {
        return condicion;
    }

    public void setCondicion(List<Condicion> condicion) {
        this.condicion = condicion;
    }

    public boolean isEsPensionado() {
        return esPensionado;
    }

    public void setEsPensionado(boolean esPensionado) {
        this.esPensionado = esPensionado;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", condicion=" + condicion +  // Cambié aquí para mostrar la lista de condiciones
                ", esPensionado=" + esPensionado +
                '}';
    }

    // Enum para las condiciones
    public enum Condicion {
        HIPERTENSION,
        DIABETES,
        ARTRITIS,
        CANCER,
        ALZHEIMER,
        NINGUNA
    }
}



