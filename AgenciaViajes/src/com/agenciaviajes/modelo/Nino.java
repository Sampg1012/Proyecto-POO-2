package com.agenciaviajes.modelo;

/**
 * Pasajero de tipo Nino. Requiere el nombre de un acompanante adulto.
 */
public class Nino extends Pasajero {

    private static final long serialVersionUID = 1L;

    private String acompanante;

    public Nino(String id, String nombre, int edad, String contacto, String acompanante) {
        super(id, nombre, edad, contacto);
        this.acompanante = acompanante;
    }

    public String getAcompanante() {
        return acompanante;
    }

    public void setAcompanante(String acompanante) {
        this.acompanante = acompanante;
    }

    @Override
    public boolean validarDatos() {
        // Un nino debe ser menor de 18 anios y debe tener registrado un acompanante
        return getEdad() >= 0 && getEdad() < 18
                && getId() != null && !getId().trim().isEmpty()
                && getNombre() != null && !getNombre().trim().isEmpty()
                && acompanante != null && !acompanante.trim().isEmpty();
    }

    @Override
    public String getTipo() {
        return "Nino";
    }

    @Override
    public String toString() {
        return super.toString() + " - Acompanante: " + acompanante;
    }
}
