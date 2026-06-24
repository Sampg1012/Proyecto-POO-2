package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa una reserva realizada por un usuario, asociada a un itinerario,
 * uno o varios pasajeros, y la asignacion de asientos correspondiente.
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum EstadoReserva {
        ACTIVA, CANCELADA, FINALIZADA
    }

    private String id;
    private Usuario usuario;
    private List<Pasajero> pasajeros;
    private Itinerario itinerario;
    private LocalDate fechaReserva;
    private EstadoReserva estado;
    // Mapa: pasajero (id) -> asiento asignado
    private Map<String, Asiento> asientosAsignados;

    public Reserva(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
        this.pasajeros = new ArrayList<>();
        this.itinerario = new Itinerario();
        this.fechaReserva = LocalDate.now();
        this.estado = EstadoReserva.ACTIVA;
        this.asientosAsignados = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Map<String, Asiento> getAsientosAsignados() {
        return asientosAsignados;
    }

    /**
     * Agrega un pasajero a la reserva, validando previamente sus datos.
     * @return true si el pasajero fue agregado, false si los datos no son validos.
     */
    public boolean agregarPasajero(Pasajero pasajero) {
        if (pasajero != null && pasajero.validarDatos()) {
            pasajeros.add(pasajero);
            return true;
        }
        return false;
    }

    public void eliminarPasajero(Pasajero pasajero) {
        pasajeros.remove(pasajero);
        if (pasajero != null) {
            Asiento asientoAsignado = asientosAsignados.remove(pasajero.getId());
            if (asientoAsignado != null) {
                asientoAsignado.liberar();
            }
        }
    }

    /**
     * Asigna un asiento disponible a un pasajero, evitando que un mismo
     * asiento sea asignado a mas de un pasajero.
     */
    public boolean asignarAsientoAPasajero(Pasajero pasajero, Asiento asiento) {
        if (pasajero == null || asiento == null) {
            return false;
        }
        if (!pasajeros.contains(pasajero)) {
            return false;
        }
        if (asientosAsignados.containsKey(pasajero.getId())) {
            return false; // Un pasajero solo puede tener un asiento
        }
        if (asientosAsignados.containsValue(asiento)) {
            return false; // El asiento ya esta asignado a otro pasajero
        }
        if (!asiento.reservar()) {
            return false; // El asiento no esta disponible
        }
        asientosAsignados.put(pasajero.getId(), asiento);
        return true;
    }

    /**
     * Calcula el valor informativo total de la reserva con base en las tarifas
     * de referencia de los vuelos del itinerario, sumando el recargo
     * correspondiente a la categoria de cada asiento asignado (Economica,
     * Ejecutiva o Primera Clase). De esta manera, la categoria elegida por
     * el pasajero influye en el valor final de la reserva.
     */
    public double calcularTotal() {
        double total = itinerario.calcularCostoTotal();
        for (Asiento asiento : asientosAsignados.values()) {
            Vuelo vuelo = buscarVueloDelAsiento(asiento);
            if (vuelo != null) {
                total += asiento.calcularRecargo(vuelo.calcularTarifaFinal());
            }
        }
        return total;
    }

    /**
     * Busca, dentro de los vuelos del itinerario, a cual de ellos pertenece
     * el asiento indicado (cada asiento pertenece a un unico vuelo).
     */
    private Vuelo buscarVueloDelAsiento(Asiento asiento) {
        for (Vuelo vuelo : itinerario.getVuelos()) {
            if (vuelo.getAsientos().contains(asiento)) {
                return vuelo;
            }
        }
        return null;
    }

    /**
     * Confirma la reserva si tiene al menos un vuelo y un pasajero.
     */
    public boolean confirmarReserva() {
        if (itinerario.estaVacio() || pasajeros.isEmpty()) {
            return false;
        }
        this.estado = EstadoReserva.ACTIVA;
        return true;
    }

    /**
     * Cancela la reserva y libera todos los asientos asociados.
     */
    public void cancelar() {
        for (Asiento asiento : asientosAsignados.values()) {
            asiento.liberar();
        }
        asientosAsignados.clear();
        this.estado = EstadoReserva.CANCELADA;
    }

    @Override
    public String toString() {
        return "Reserva " + id + " | Usuario: " + usuario.getLogin()
                + " | Fecha: " + fechaReserva
                + " | Estado: " + estado
                + " | Pasajeros: " + pasajeros.size()
                + " | Total: $" + String.format("%.2f", calcularTotal());
    }
}
