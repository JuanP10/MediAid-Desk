package org.example.mediaid.modelos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Medicamento {
    private String nombre;
    private int cantidad;
    private TipoMedicamento tipo;
    private LocalDate fechaVencimiento;
    private FormaAdquisicion formaAdquisicion;

    // Constructor vacío necesario para Jackson
    public Medicamento() {
    }

    // Constructor completo con anotaciones para Jackson
    @JsonCreator
    public Medicamento(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("cantidad") int cantidad,
            @JsonProperty("tipo") TipoMedicamento tipo,
            @JsonProperty("fechaVencimiento") LocalDate fechaVencimiento,
            @JsonProperty("formaAdquisicion") FormaAdquisicion formaAdquisicion) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.fechaVencimiento = fechaVencimiento;
        this.formaAdquisicion = formaAdquisicion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        } else {
            System.out.println("La cantidad no puede ser negativa.");
        }
    }

    public TipoMedicamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMedicamento tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public FormaAdquisicion getFormaAdquisicion() {
        return formaAdquisicion;
    }

    public void setFormaAdquisicion(FormaAdquisicion formaAdquisicion) {
        this.formaAdquisicion = formaAdquisicion;
    }

    // Método para verificar si el medicamento está vencido
    public boolean estaVencido() {
        LocalDate fechaActual = LocalDate.now();
        return fechaVencimiento.isBefore(fechaActual);
    }

    // Método para reducir la cantidad del medicamento
    public boolean reducirCantidad(int cantidad) {
        if (cantidad > 0 && this.cantidad >= cantidad) {
            this.cantidad -= cantidad;
            return true;
        } else {
            System.out.println("Cantidad insuficiente o no válida.");
            return false;
        }
    }

    // Método para aumentar la cantidad del medicamento
    public void aumentarCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad += cantidad;
        } else {
            System.out.println("La cantidad a agregar debe ser positiva.");
        }
    }

    // Método para mostrar la información del medicamento
    @Override
    public String toString() {
        return "Medicamento{" +
                "nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", tipo=" + tipo +
                ", fechaVencimiento=" + fechaVencimiento +
                ", formaAdquisicion=" + formaAdquisicion +
                '}';
    }

    // Enums para el tipo de medicamento y forma de adquisición
    public enum TipoMedicamento {
        ANALGESICO,
        ANTIBIOTICO,
        ANTISEPTICO,
        ANTIINFLAMATORIO,
    }

    public enum FormaAdquisicion {
        COMPRA,
        DONACION,
    }
}
