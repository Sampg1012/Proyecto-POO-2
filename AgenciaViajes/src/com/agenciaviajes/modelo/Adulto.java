package com.agenciaviajes.modelo;

/**
 * Pasajero de tipo Adulto. No tiene atributos adicionales,
 * pero implementa su propia validacion de datos.
 */
public class Adulto extends Pasajero {

    private static final long serialVersionUID = 1L;

    public Adulto(String id, String nombre, int edad, String contacto) {
        super(id, nombre, edad, contacto);
    }

    @Override
    public boolean validarDatos() {
        // Un adulto debe tener al menos 18 anios, id, nombre y contacto validos
        return getEdad() >= 18
                && getId() != null && !getId().trim().isEmpty()
                && getNombre() != null && !getNombre().trim().isEmpty()
                && getContacto() != null && !getContacto().trim().isEmpty();
    }

    @Override
    public String getTipo() {
        return "Adulto";
    }
}