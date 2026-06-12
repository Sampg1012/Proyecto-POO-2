package com.agenciaviajes.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del sistema. Administra los usuarios registrados,
 * las aerolineas, los vuelos y las reservas de la agencia.
 * Funciona como fachada (controlador del modelo) para la interfaz grafica.
 */
public class AgenciaViajes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private List<Usuario> usuarios;
    private List<Aerolinea> aerolineas;
    private List<Vuelo> vuelos;
    private List<Reserva> reservas;
    private Usuario usuarioActual;

    public AgenciaViajes(String nombre) {
        this.nombre = nombre;
        this.usuarios = new ArrayList<>();
        this.aerolineas = new ArrayList<>();
        this.vuelos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.usuarioActual = null;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // ---------------------------------------------------------------
    // Gestion de usuarios
    // ---------------------------------------------------------------

    /**
     * Registra un nuevo usuario, validando que el login no exista previamente.
     */
    public boolean registrarse(String nombres, String email, String login, String contrasena) {
        if (buscarUsuarioPorLogin(login) != null) {
            return false; // El login ya esta en uso
        }
        int nuevoId = usuarios.size() + 1;
        Usuario usuario = new Usuario(nuevoId, nombres, email, login, contrasena);
        usuarios.add(usuario);
        return true;
    }

    /**
     * Inicia sesion validando login y contrasena.
     * @return true si las credenciales son correctas.
     */
    public boolean iniciarSesion(String login, String contrasena) {
        Usuario usuario = buscarUsuarioPorLogin(login);
        if (usuario != null && usuario.validarCredenciales(login, contrasena)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    /**
     * Cierra la sesion del usuario actual.
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario buscarUsuarioPorLogin(String login) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos del usuario actual (modificar registro).
     */
    public boolean actualizarUsuarioActual(String nombres, String email, String contrasena) {
        if (usuarioActual == null) {
            return false;
        }
        if (nombres != null && !nombres.trim().isEmpty()) {
            usuarioActual.setNombres(nombres);
        }
        if (email != null && !email.trim().isEmpty()) {
            usuarioActual.setEmail(email);
        }
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuarioActual.setContrasena(contrasena);
        }
        return true;
    }

    /**
     * Cancela el registro (cuenta) del usuario actual.
     */
    public boolean cancelarRegistroUsuarioActual() {
        if (usuarioActual == null) {
            return false;
        }
        // Cancela todas las reservas activas del usuario antes de eliminarlo
        for (Reserva r : usuarioActual.getReservas()) {
            if (r.getEstado() == Reserva.EstadoReserva.ACTIVA) {
                r.cancelar();
            }
        }
        usuarios.remove(usuarioActual);
        usuarioActual = null;
        return true;
    }

    // ---------------------------------------------------------------
    // Gestion de aerolineas y vuelos
    // ---------------------------------------------------------------

    public void agregarAerolinea(Aerolinea aerolinea) {
        aerolineas.add(aerolinea);
    }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
        vuelo.getAerolinea().agregarVuelo(vuelo);
    }

    /**
     * Consulta vuelos disponibles entre dos ciudades, con filtros opcionales
     * por fecha, aerolinea y preferencia de vuelo directo.
     */
    public List<Vuelo> consultarVuelosPorHorario(String origen, String destino,
                                                   LocalDate fecha, String aerolinea,
                                                   Boolean soloDirectos) {
        List<Vuelo> resultado = new ArrayList<>();
        for (Vuelo v : vuelos) {
            if (!v.getOrigen().equalsIgnoreCase(origen)) continue;
            if (!v.getDestino().equalsIgnoreCase(destino)) continue;
            if (fecha != null && !v.getFecha().equals(fecha)) continue;
            if (aerolinea != null && !aerolinea.trim().isEmpty()
                    && !v.getAerolinea().getNombre().equalsIgnoreCase(aerolinea)) continue;
            if (soloDirectos != null && soloDirectos && !v.isEsDirecto()) continue;
            resultado.add(v);
        }
        return resultado;
    }

    /**
     * Consulta vuelos entre dos ciudades ordenados por tarifa de referencia (menor a mayor).
     */
    public List<Vuelo> consultarVuelosPorTarifa(String origen, String destino) {
        List<Vuelo> resultado = consultarVuelosPorHorario(origen, destino, null, null, null);
        resultado.sort((v1, v2) -> Double.compare(v1.calcularTarifaFinal(), v2.calcularTarifaFinal()));
        return resultado;
    }

    /**
     * Genera vuelos aleatorios que respetan el origen, destino y filtros opcionales
     * indicados por el usuario.
     */
    public List<Vuelo> generarVuelosAleatorios(String origen, String destino,
                                               LocalDate fecha, String aerolinea,
                                               int cantidad) {
        List<Vuelo> generados = GeneradorVuelos.generarVuelos(origen, destino, fecha, aerolinea, cantidad);
        for (Vuelo vuelo : generados) {
            agregarVuelo(vuelo);
        }
        return generados;
    }

    /**
     * Busca un vuelo por su identificador.
     */
    public Vuelo buscarVueloPorId(String id) {
        for (Vuelo v : vuelos) {
            if (v.getId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Gestion de reservas
    // ---------------------------------------------------------------

    /**
     * Crea una nueva reserva vacia asociada al usuario actual.
     */
    public Reserva crearReserva() {
        if (usuarioActual == null) {
            return null;
        }
        String nuevoId = "R" + String.format("%04d", reservas.size() + 1);
        Reserva reserva = new Reserva(nuevoId, usuarioActual);
        reservas.add(reserva);
        usuarioActual.agregarReserva(reserva);
        return reserva;
    }

    /**
     * Cancela una reserva del usuario actual y libera los asientos asociados.
     */
    public boolean cancelarReserva(Reserva reserva) {
        if (reserva == null || usuarioActual == null) {
            return false;
        }
        if (!usuarioActual.getReservas().contains(reserva)) {
            return false;
        }
        reserva.cancelar();
        return true;
    }

    /**
     * Devuelve las reservas activas del usuario actual.
     */
    public List<Reserva> verReservas() {
        if (usuarioActual == null) {
            return new ArrayList<>();
        }
        return usuarioActual.getReservas();
    }

    // ---------------------------------------------------------------
    // Datos de ejemplo para pruebas (aerolineas, vuelos y asientos)
    // ---------------------------------------------------------------

    /**
     * Carga datos de ejemplo (aerolineas, vuelos y asientos) si no existen vuelos
     * registrados. Util para pruebas iniciales de la aplicacion.
     */
    public void cargarDatosDemo() {
        if (!vuelos.isEmpty()) {
            return;
        }

        Aerolinea avianca = new Aerolinea("Avianca", "AV");
        Aerolinea latam = new Aerolinea("LATAM", "LA");
        agregarAerolinea(avianca);
        agregarAerolinea(latam);

        VueloNacional v1 = new VueloNacional("V001", "Bogota", "Medellin",
                LocalDate.now().plusDays(2), LocalTime.of(8, 0), LocalTime.of(9, 5),
                180000, avianca, true, 0.08);
        crearAsientosBasicos(v1);
        agregarVuelo(v1);

        VueloNacional v2 = new VueloNacional("V002", "Bogota", "Cartagena",
                LocalDate.now().plusDays(3), LocalTime.of(14, 30), LocalTime.of(16, 0),
                220000, latam, true, 0.08);
        crearAsientosBasicos(v2);
        agregarVuelo(v2);

        VueloInternacional v3 = new VueloInternacional("V003", "Bogota", "Madrid",
                LocalDate.now().plusDays(5), LocalTime.of(22, 0), LocalTime.of(14, 30),
                2500000, avianca, true, 0.15);
        crearAsientosBasicos(v3);
        agregarVuelo(v3);

        VueloInternacional v4 = new VueloInternacional("V004", "Cartagena", "Miami",
                LocalDate.now().plusDays(4), LocalTime.of(10, 15), LocalTime.of(13, 45),
                1800000, latam, false, 0.15);
        v4.getCiudadesEscala().add("Panama");
        crearAsientosBasicos(v4);
        agregarVuelo(v4);
    }

    private void crearAsientosBasicos(Vuelo vuelo) {
        for (int i = 1; i <= 4; i++) {
            vuelo.agregarAsiento(new Economico("E" + i));
        }
        for (int i = 1; i <= 2; i++) {
            vuelo.agregarAsiento(new Ejecutivo("J" + i));
        }
        vuelo.agregarAsiento(new PrimeraClase("P1"));
    }
}