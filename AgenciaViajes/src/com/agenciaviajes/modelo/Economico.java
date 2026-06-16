package com.agenciaviajes.modelo;

/**
 * Asiento de categoria Economica.
 */
public class Economico extends Asiento {

    private static final long serialVersionUID = 1L;

    public Economico(String numero) {
        super(numero);
    }

    /**
     * Informacion de equipaje de mano permitido para esta categoria.
     */
    public String equipamientoDeMano() {
        return "1 pieza de equipaje de mano (hasta 8 kg)";
    }

    @Override
    public String getCategoriaNombre() {
        return "Economica";
    }

    @Override
    public String getServicios() {
        return equipamientoDeMano();
    }

    @Override
    public double getFactorPrecio() {
        return 1.0;
}
}