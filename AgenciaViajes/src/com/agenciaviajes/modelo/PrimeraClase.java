package com.agenciaviajes.modelo;

/**
 * Asiento de categoria Primera Clase.
 */
public class PrimeraClase extends Asiento {

    private static final long serialVersionUID = 1L;

    public PrimeraClase(String numero) {
        super(numero);
    }

    /**
     * Descripcion del menu exclusivo de esta categoria.
     */
    public String menuExclusivo() {
        return "Menu gourmet exclusivo y servicio personalizado";
    }

    @Override
    public String getCategoriaNombre() {
        return "Primera Clase";
    }

    @Override
    public String getServicios() {
        return menuExclusivo() + " + Acceso a sala VIP + 3 piezas de equipaje";
    }
}
