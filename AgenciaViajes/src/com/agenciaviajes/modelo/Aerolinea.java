package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una aerolinea que ofrece uno o varios vuelos.
 */
public class Aerolinea implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String codigo;
    private List<Vuelo> vuelos;

    public Aerolinea(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.vuelos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }

    /**
     * Devuelve la lista de vuelos ofrecidos por esta aerolinea.
     */
    public List<Vuelo> consultarVuelos() {
        return vuelos;
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
