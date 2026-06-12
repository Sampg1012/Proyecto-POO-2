package com.agenciaviajes.modelo;

import java.io.Serializable;

/**
 * Clase abstracta que representa un pasajero dentro de una reserva.
 * Se especializa en Adulto, Nino y AdultoMayor (herencia y polimorfismo
 * a traves del metodo validarDatos()).
 */
public abstract class Pasajero implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private int edad;
    private String contacto;

    public Pasajero(String id, String nombre, int edad, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.contacto = contacto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    /**
     * Cada tipo de pasajero valida sus datos segun sus propias reglas
     * (metodo abstracto -> polimorfismo).
     */
    public abstract boolean validarDatos();

    /**
     * Devuelve una descripcion del tipo de pasajero, util para mostrar
     * en la interfaz grafica.
     */
    public abstract String getTipo();

    @Override
    public String toString() {
        return getTipo() + " - " + nombre + " (ID: " + id + ", Edad: " + edad + ")";
    }
}