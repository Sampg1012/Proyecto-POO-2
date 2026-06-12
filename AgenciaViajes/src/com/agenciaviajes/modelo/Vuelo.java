package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un vuelo ofrecido por una aerolinea.
 * Se especializa en VueloNacional y VueloInternacional, que difieren
 * en el calculo de la tarifa final (polimorfismo mediante calcularTarifaFinal()).
 */
public abstract class Vuelo implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum EstadoVuelo {
        PROGRAMADO, EN_HORA, RETRASADO, CANCELADO, FINALIZADO
    }

    private String id;
    private String origen;
    private String destino;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private double tarifaBase;
    private EstadoVuelo estado;
    private Aerolinea aerolinea;
    private List<Asiento> asientos;
    private boolean esDirecto;
    private List<String> ciudadesEscala;

    public Vuelo(String id, String origen, String destino, LocalDate fecha,
                  LocalTime horaSalida, LocalTime horaLlegada, double tarifaBase,
                  Aerolinea aerolinea, boolean esDirecto) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.tarifaBase = tarifaBase;
        this.estado = EstadoVuelo.PROGRAMADO;
        this.aerolinea = aerolinea;
        this.asientos = new ArrayList<>();
        this.esDirecto = esDirecto;
        this.ciudadesEscala = new ArrayList<>();
    }

    // ---- Getters y setters ----

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public void setTarifaBase(double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    public EstadoVuelo getEstado() {
        return estado;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public boolean isEsDirecto() {
        return esDirecto;
    }

    public void setEsDirecto(boolean esDirecto) {
        this.esDirecto = esDirecto;
    }

    public List<String> getCiudadesEscala() {
        return ciudadesEscala;
    }

    public void agregarAsiento(Asiento asiento) {
        asientos.add(asiento);
    }

    /**
     * Calcula la duracion del vuelo a partir de la hora de salida y llegada.
     */
    public Duration calcularDuracion() {
        if (horaSalida == null || horaLlegada == null) {
            return Duration.ZERO;
        }
        Duration duracion = Duration.between(horaSalida, horaLlegada);
        if (duracion.isNegative()) {
            // El vuelo llega al dia siguiente
            duracion = duracion.plusHours(24);
        }
        return duracion;
    }

    /**
     * Indica si el vuelo tiene asientos disponibles.
     */
    public boolean consultarDisponibilidad() {
        return obtenerAsientosDisponibles().size() > 0;
    }

    /**
     * Cambia el estado del vuelo (programado, en hora, retrasado, etc.).
     */
    public void cambiarEstado(EstadoVuelo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     * Devuelve la lista de asientos disponibles para este vuelo.
     */
    public List<Asiento> obtenerAsientosDisponibles() {
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.isDisponible()) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    /**
     * Devuelve la lista de asientos disponibles filtrados por categoria.
     */
    public List<Asiento> obtenerAsientosDisponiblesPorCategoria(String categoria) {
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.isDisponible() && a.getCategoria().equalsIgnoreCase(categoria)) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    /**
     * Asigna (reserva) un asiento especifico, si esta disponible.
     */
    public boolean asignarAsiento(Asiento asiento) {
        if (asientos.contains(asiento)) {
            return asiento.reservar();
        }
        return false;
    }

    /**
     * Calcula la tarifa final del vuelo aplicando los impuestos
     * o recargos correspondientes segun el tipo de vuelo.
     * Cada subclase implementa su propia logica (polimorfismo).
     */
    public abstract double calcularTarifaFinal();

    /**
     * Devuelve un texto descriptivo del tipo de vuelo (Nacional/Internacional).
     */
    public abstract String getTipoVuelo();

    @Override
    public String toString() {
        return id + " | " + origen + " -> " + destino + " | " + fecha
                + " | " + horaSalida + " - " + horaLlegada
                + " | " + aerolinea.getNombre()
                + " | Tarifa final: $" + String.format("%.2f", calcularTarifaFinal())
                + " | " + estado
                + " | " + (esDirecto ? "Directo" : "Con escala");
    }
}