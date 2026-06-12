package com.agenciaviajes.modelo;

/**
 * Asiento de categoria Ejecutiva.
 */
public class Ejecutivo extends Asiento {

    private static final long serialVersionUID = 1L;

    public Ejecutivo(String numero) {
        super(numero);
    }

    /**
     * Acceso a sala VIP, beneficio exclusivo de esta categoria.
     */
    public String accesoSalaVip() {
        return "Acceso a sala VIP del aeropuerto";
    }

    @Override
    public String getCategoriaNombre() {
        return "Ejecutiva";
    }

    @Override
    public String getServicios() {
        return accesoSalaVip() + " + 2 piezas de equipaje (hasta 23 kg c/u)";
    }
}
