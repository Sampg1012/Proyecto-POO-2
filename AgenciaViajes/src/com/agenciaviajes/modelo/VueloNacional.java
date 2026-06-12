package com.agenciaviajes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Vuelo dentro del mismo pais. Aplica un impuesto nacional sobre la tarifa base.
 */
public class VueloNacional extends Vuelo {

    private static final long serialVersionUID = 1L;

    private double impuestoNacional; // porcentaje, ej. 0.08 = 8%

    public VueloNacional(String id, String origen, String destino, LocalDate fecha,
                          LocalTime horaSalida, LocalTime horaLlegada, double tarifaBase,
                          Aerolinea aerolinea, boolean esDirecto, double impuestoNacional) {
        super(id, origen, destino, fecha, horaSalida, horaLlegada, tarifaBase, aerolinea, esDirecto);
        this.impuestoNacional = impuestoNacional;
    }

    public VueloNacional(String id, String origen, String destino, LocalDate fecha,
                          LocalTime horaSalida, LocalTime horaLlegada, double tarifaBase,
                          Aerolinea aerolinea, boolean esDirecto) {
        this(id, origen, destino, fecha, horaSalida, horaLlegada, tarifaBase, aerolinea, esDirecto,
                0.08 + Math.random() * 0.02);
    }

    public double getImpuestoNacional() {
        return impuestoNacional;
    }

    public void setImpuestoNacional(double impuestoNacional) {
        this.impuestoNacional = impuestoNacional;
    }

    @Override
    public double calcularTarifaFinal() {
        return getTarifaBase() * (1 + impuestoNacional);
    }

    @Override
    public String getTipoVuelo() {
        return "Nacional";
    }
}