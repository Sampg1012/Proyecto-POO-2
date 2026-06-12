package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario registrado en el sistema de la agencia de viajes.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombres;
    private String email;
    private String login;
    private String contrasena;
    private List<Reserva> reservas;

    public Usuario(int id, String nombres, String email, String login, String contrasena) {
        this.id = id;
        this.nombres = nombres;
        this.email = email;
        this.login = login;
        this.contrasena = contrasena;
        this.reservas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    /**
     * Valida que el login y la contrasena ingresados coincidan con los del usuario.
     */
    public boolean validarCredenciales(String login, String contrasena) {
        return this.login.equals(login) && this.contrasena.equals(contrasena);
    }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    /**
     * Devuelve unicamente las reservas activas del usuario.
     */
    public List<Reserva> verReservasActivas() {
        List<Reserva> activas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getEstado() == Reserva.EstadoReserva.ACTIVA) {
                activas.add(r);
            }
        }
        return activas;
    }

    @Override
    public String toString() {
        return nombres + " (" + login + ")";
    }
}
