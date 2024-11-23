package org.example.mediaid.modelos;

import java.time.LocalDate;

public class Suministro {
    private Paciente paciente;
    private Medicamento medicamento;
    private int cantidad;
    private LocalDate fechaSuministro;
    private Enfermero enfermero;

    public Suministro() {}
    // Constructor
    public Suministro(Paciente paciente, Medicamento medicamento, int cantidad, LocalDate fechaSuministro, Enfermero enfermero) {
        this.paciente = paciente;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.fechaSuministro = fechaSuministro;
        this.enfermero = enfermero;
    }

    // Getters y Setters
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaSuministro() {
        return fechaSuministro;
    }

    public void setFechaSuministro(LocalDate fechaSuministro) {
        this.fechaSuministro = fechaSuministro;
    }

    public Enfermero getEnfermero() {
        return enfermero;
    }

    public void setEnfermero(Enfermero enfermero) {
        this.enfermero = enfermero;
    }

    // Método toString para representar el suministro
    @Override
    public String toString() {
        return "Suministro{" +
                "paciente=" + paciente.getNombre() +
                ", medicamento=" + medicamento.getNombre() +
                ", cantidad=" + cantidad +
                ", fechaSuministro=" + fechaSuministro +
                ", enfermero=" + enfermero.getNombre() +
                '}';
    }
}
