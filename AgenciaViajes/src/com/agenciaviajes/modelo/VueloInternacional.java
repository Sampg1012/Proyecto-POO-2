package com.agenciaviajes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Vuelo entre paises distintos. Aplica un impuesto internacional sobre la tarifa base.
 */
public class VueloInternacional extends Vuelo {

    private static final long serialVersionUID = 1L;

    private double impuestoInternacional; // porcentaje, ej. 0.15 = 15%

    public VueloInternacional(String id, String origen, String destino, LocalDate fecha,
                               LocalTime horaSalida, LocalTime horaLlegada, double tarifaBase,
                               Aerolinea aerolinea, boolean esDirecto, double impuestoInternacional) {
        super(id, origen, destino, fecha, horaSalida, horaLlegada, tarifaBase, aerolinea, esDirecto);
        this.impuestoInternacional = impuestoInternacional;
    }

    public VueloInternacional(String id, String origen, String destino, LocalDate fecha,
                               LocalTime horaSalida, LocalTime horaLlegada, double tarifaBase,
                               Aerolinea aerolinea, boolean esDirecto) {
        this(id, origen, destino, fecha, horaSalida, horaLlegada, tarifaBase, aerolinea, esDirecto,
                0.12 + Math.random() * 0.08);
    }

    public double getImpuestoInternacional() {
        return impuestoInternacional;
    }

    public void setImpuestoInternacional(double impuestoInternacional) {
        this.impuestoInternacional = impuestoInternacional;
    }

    @Override
    public double calcularTarifaFinal() {
        return getTarifaBase() * (1 + impuestoInternacional);
    }

    @Override
    public String getTipoVuelo() {
        return "Internacional";
    }
}

