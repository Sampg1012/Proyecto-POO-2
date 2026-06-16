package com.agenciaviajes.modelo;

import java.io.Serializable;

/**
 * Clase abstracta que representa un asiento dentro de un vuelo.
 * Se especializa en Economico, Ejecutivo y Primera_Clase, cada una
 * con servicios particulares (polimorfismo mediante getServicios()).
 */
public abstract class Asiento implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String numero;
    protected String categoria;
    protected boolean disponible;

    public Asiento(String numero) {
        this.numero = numero;
        this.disponible = true;
        this.categoria = getCategoriaNombre();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Marca el asiento como reservado (no disponible).
     * @return true si se pudo reservar, false si ya estaba ocupado.
     */
    public boolean reservar() {
        if (disponible) {
            disponible = false;
            return true;
        }
        return false;
    }

    /**
     * Libera el asiento, dejandolo disponible nuevamente.
     */
    public void liberar() {
        disponible = true;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    /**
     * Nombre de la categoria, definido por cada subclase.
     */
    public abstract String getCategoriaNombre();

    /**
     * Descripcion de los servicios o beneficios incluidos
     * segun la categoria del asiento (polimorfismo).
     */
    public abstract String getServicios();

    public abstract double getFactorPrecio();

    public double calcularRecargo(double tarifaFinalVuelo) {
        return tarifaFinalVuelo * (getFactorPrecio() - 1.0);
    }

    public double calcularPrecio(double tarifaFinalVuelo) {
        return tarifaFinalVuelo * getFactorPrecio();
    }
    
    @Override
    public String toString() {
        return "Asiento " + numero + " [" + categoria + "] - "
                + (disponible ? "Disponible" : "Ocupado");
    }
}