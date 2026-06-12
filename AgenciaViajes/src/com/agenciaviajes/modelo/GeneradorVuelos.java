package com.agenciaviajes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorVuelos {

    private static final Random random = new Random();

    public static List<Vuelo> generarVuelos(int cantidad) {
        return generarVuelos(null, null, null, null, cantidad);
    }

    public static List<Vuelo> generarVuelos(String origen, String destino,
                                            LocalDate fecha, String aerolinea,
                                            int cantidad) {
        List<Vuelo> vuelos = new ArrayList<>();

        String origenBase = normalizarTexto(origen);
        String destinoBase = normalizarTexto(destino);

        for (int i = 0; i < cantidad; i++) {
            String origenFinal = origenBase.isEmpty() ? obtenerOrigen() : origenBase;
            String destinoFinal = destinoBase.isEmpty()
                    ? (CatalogoDatos.esDestinoInternacional(CatalogoDatos.normalizar(origenFinal))
                            ? obtenerDestinoInternacional()
                            : obtenerDestinoNacional(origenFinal))
                    : destinoBase;

            if (origenFinal.equalsIgnoreCase(destinoFinal)) {
                destinoFinal = obtenerDestinoNacional(origenFinal);
            }

            boolean internacional = CatalogoDatos.esDestinoInternacional(CatalogoDatos.normalizar(destinoFinal));
            LocalTime salida = LocalTime.of(random.nextInt(23), random.nextInt(60));
            LocalTime llegada = salida.plusHours(1 + random.nextInt(10));

            double tarifa = CatalogoDatos.generarTarifaReferencia(origenFinal, destinoFinal);
            Aerolinea aero = obtenerAerolineaAleatoria(aerolinea, internacional);
            LocalDate fechaVuelo = (fecha != null) ? fecha : LocalDate.now().plusDays(random.nextInt(30));

            Vuelo vuelo;
            if (internacional) {
                vuelo = new VueloInternacional(
                        "INT-" + (1000 + i), origenFinal, destinoFinal,
                        fechaVuelo, salida, llegada, tarifa, aero, random.nextBoolean());
            } else {
                vuelo = new VueloNacional(
                        "NAL-" + (1000 + i), origenFinal, destinoFinal,
                        fechaVuelo, salida, llegada, tarifa, aero, random.nextBoolean());
            }

            if (!vuelo.isEsDirecto()) {
                vuelo.getCiudadesEscala().add(CatalogoDatos.obtenerEscalaAleatoria());
            }

            crearAsientosBasicos(vuelo);
            vuelos.add(vuelo);
        }
        return vuelos;
    }

    private static String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private static void crearAsientosBasicos(Vuelo vuelo) {
        for (int i = 1; i <= 4; i++) {
            vuelo.agregarAsiento(new Economico("E" + i));
        }
        for (int i = 1; i <= 2; i++) {
            vuelo.agregarAsiento(new Ejecutivo("J" + i));
        }
        vuelo.agregarAsiento(new PrimeraClase("P1"));
    }

    private static Aerolinea obtenerAerolineaAleatoria(String aerolineaFiltro, boolean internacional) {
        List<Aerolinea> opciones = internacional
                ? CatalogoDatos.AEROLINEAS_INTERNACIONALES
                : CatalogoDatos.AEROLINEAS_NACIONALES;

        if (aerolineaFiltro != null && !aerolineaFiltro.trim().isEmpty()) {
            String filtro = aerolineaFiltro.trim();
            for (Aerolinea aero : opciones) {
                if (aero.getNombre().equalsIgnoreCase(filtro)) {
                    return aero;
                }
            }
        }

        return opciones.get(random.nextInt(opciones.size()));
    }

    private static String obtenerOrigen() {
        List<String> lista = new ArrayList<>(CatalogoDatos.CIUDADES_NACIONALES.keySet());
        return lista.get(random.nextInt(lista.size()));
    }

    private static String obtenerDestinoNacional(String origen) {
        String destino;
        do {
            destino = obtenerOrigen();
        } while (destino.equals(origen));
        return destino;
    }

    private static String obtenerDestinoInternacional() {
        List<String> lista = new ArrayList<>(CatalogoDatos.DESTINOS_INTERNACIONALES.keySet());
        return lista.get(random.nextInt(lista.size()));
    }
}
