package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el itinerario de una reserva, compuesto por uno o varios vuelos
 * (vuelo directo o vuelos conectados).
 */
public class Itinerario implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Vuelo> vuelos;

    public Itinerario() {
        this.vuelos = new ArrayList<>();
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }

    public void eliminarVuelo(Vuelo vuelo) {
        vuelos.remove(vuelo);
    }

    /**
     * Calcula la duracion total del itinerario sumando la duracion de cada vuelo.
     */
    public Duration calcularDuracionTotal() {
        Duration total = Duration.ZERO;
        for (Vuelo v : vuelos) {
            total = total.plus(v.calcularDuracion());
        }
        return total;
    }

    /**
     * Calcula el total informativo de tarifas de todos los vuelos del itinerario.
     */
    public double calcularCostoTotal() {
        double total = 0;
        for (Vuelo v : vuelos) {
            total += v.calcularTarifaFinal();
        }
        return total;
    }

    public boolean estaVacio() {
        return vuelos.isEmpty();
    }

    @Override
    public String toString() {
        if (vuelos.isEmpty()) {
            return "Itinerario vacio";
        }
        StringBuilder sb = new StringBuilder();
        for (Vuelo v : vuelos) {
            sb.append(v.getOrigen()).append(" -> ").append(v.getDestino()).append(" | ");
        }
        return sb.toString();
    }
}