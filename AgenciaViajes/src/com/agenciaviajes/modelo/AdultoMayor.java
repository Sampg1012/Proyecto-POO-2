package com.agenciaviajes.modelo;

/**
 * Pasajero de tipo Adulto Mayor. Puede requerir asistencia especial.
 */
public class AdultoMayor extends Pasajero {

    private static final long serialVersionUID = 1L;

    private boolean requiereAsistencia;

    public AdultoMayor(String id, String nombre, int edad, String contacto, boolean requiereAsistencia) {
        super(id, nombre, edad, contacto);
        this.requiereAsistencia = requiereAsistencia;
    }

    public boolean isRequiereAsistencia() {
        return requiereAsistencia;
    }

    public void setRequiereAsistencia(boolean requiereAsistencia) {
        this.requiereAsistencia = requiereAsistencia;
    }

    @Override
    public boolean validarDatos() {
        // Un adulto mayor debe tener 60 anios o mas y datos basicos completos
        return getEdad() >= 60
                && getId() != null && !getId().trim().isEmpty()
                && getNombre() != null && !getNombre().trim().isEmpty()
                && getContacto() != null && !getContacto().trim().isEmpty();
    }

    @Override
    public String getTipo() {
        return "Adulto Mayor";
    }

    @Override
    public String toString() {
        return super.toString() + " - Requiere asistencia: " + (requiereAsistencia ? "Si" : "No");
    }
}