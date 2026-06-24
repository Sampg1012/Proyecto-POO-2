package com.agenciaviajes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneradorVuelos {

    private static final Random random = new Random();
    private static final AtomicInteger SECUENCIA_IDS = new AtomicInteger(1000);

    public static List<Vuelo> generarVuelos(int cantidad) {
        return generarVuelos(null, null, null, null, cantidad);
    }

    public static List<Vuelo> generarVuelos(String origen, String destino,
                                            LocalDate fecha, String aerolinea,
                                            int cantidad) {
        List<Vuelo> vuelos = new ArrayList<>();
        int cantidadFinal = Math.max(8, Math.min(10, cantidad));

        String origenBase = normalizarTexto(origen);
        String destinoBase = normalizarTexto(destino);

        for (int i = 0; i < cantidadFinal; i++) {
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
            LocalTime llegada = calcularLlegada(salida, internacional);

            double tarifa = CatalogoDatos.generarTarifaReferencia(origenFinal, destinoFinal);
            Aerolinea aero = obtenerAerolineaAleatoria(aerolinea, internacional);
            LocalDate fechaVuelo;
            if (fecha != null) {
                // Si el usuario solicita una fecha pasada, usar la fecha actual en su lugar
                fechaVuelo = fecha.isBefore(LocalDate.now()) ? LocalDate.now() : fecha;
            } else {
                fechaVuelo = LocalDate.now().plusDays(random.nextInt(30));
            }
            boolean esDirecto = internacional ? random.nextBoolean() : true;

            Vuelo vuelo;
            String idVuelo = generarIdVuelo(internacional);
            if (internacional) {
                vuelo = new VueloInternacional(
                        idVuelo, origenFinal, destinoFinal,
                        fechaVuelo, salida, llegada, tarifa, aero, esDirecto);
            } else {
                vuelo = new VueloNacional(
                        idVuelo, origenFinal, destinoFinal,
                        fechaVuelo, salida, llegada, tarifa, aero, esDirecto);
            }

            if (!vuelo.isEsDirecto()) {
                vuelo.getCiudadesEscala().add(CatalogoDatos.obtenerEscalaAleatoria());
            }

            crearAsientos(vuelo);
            vuelos.add(vuelo);
        }
        return vuelos;
    }

    private static LocalTime calcularLlegada(LocalTime salida, boolean internacional) {
        if (!internacional) {
            int minutosDuracion = 60 + random.nextInt(61);
            return salida.plusMinutes(minutosDuracion);
        }
        return salida.plusHours(1 + random.nextInt(10));
    }

    private static String generarIdVuelo(boolean internacional) {
        return internacional ? "INT-" + SECUENCIA_IDS.incrementAndGet() : "NAL-" + SECUENCIA_IDS.incrementAndGet();
    }

    private static String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public static void crearAsientos(Vuelo vuelo) {
        if (vuelo == null) {
            return;
        }
        boolean internacional = vuelo instanceof VueloInternacional;
        String destinoNormalizado = CatalogoDatos.normalizar(vuelo.getDestino());
        CatalogoDatos.CategoriaInternacional categoriaInternacional = CatalogoDatos.CATEGORIA_DESTINO_INTERNACIONAL
                .getOrDefault(destinoNormalizado, CatalogoDatos.CategoriaInternacional.REGIONAL);

        if (!internacional || categoriaInternacional == CatalogoDatos.CategoriaInternacional.REGIONAL) {
            // Aumentar el minimo de filas de Primera Clase para que haya mas asientos
            int filasPrimera = random.nextInt(2) + 2; // 2-3 filas primera clase
            int filasEjecutiva = random.nextInt(2) + 3; // 3-4 filas ejecutiva
            int filasEconomica = random.nextInt(6) + 22; // 22-27 filas econ
            crearSeccionAsientos(vuelo, 1, filasPrimera, new char[]{'A', 'D'}, "Primera Clase");
            crearSeccionAsientos(vuelo, filasPrimera + 1, filasEjecutiva, new char[]{'A', 'C', 'D', 'F'}, "Ejecutiva");
            crearSeccionAsientos(vuelo, filasPrimera + filasEjecutiva + 1, filasEconomica, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, "Economica");
        } else {
            int filasPrimera = random.nextInt(2) + 2; // 2-3 filas primera clase
            int filasEjecutiva = random.nextInt(3) + 6; // 6-8 filas ejecutiva
            int filasEconomica = random.nextInt(8) + 28; // 28-35 filas econ
            crearSeccionAsientos(vuelo, 1, filasPrimera, new char[]{'A', 'D'}, "Primera Clase");
            crearSeccionAsientos(vuelo, filasPrimera + 1, filasEjecutiva, new char[]{'A', 'C', 'D', 'F'}, "Ejecutiva");
            crearSeccionAsientos(vuelo, filasPrimera + filasEjecutiva + 1, filasEconomica, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, "Economica");
        }
        marcarAsientosOcupados(vuelo);
    }

    private static void crearSeccionAsientos(Vuelo vuelo, int filaInicio, int filas, char[] letras, String categoria) {
        for (int fila = filaInicio; fila < filaInicio + filas; fila++) {
            for (char letra : letras) {
                String numero = fila + String.valueOf(letra);
                switch (categoria) {
                    case "Primera Clase" -> vuelo.agregarAsiento(new PrimeraClase(numero));
                    case "Ejecutiva" -> vuelo.agregarAsiento(new Ejecutivo(numero));
                    default -> vuelo.agregarAsiento(new Economico(numero));
                }
            }
        }
    }

    private static void marcarAsientosOcupados(Vuelo vuelo) {
        if (vuelo == null) {
            return;
        }
        ocuparAsientosAleatorios(vuelo, "Primera Clase");
        ocuparAsientosAleatorios(vuelo, "Ejecutiva");
        ocuparAsientosAleatorios(vuelo, "Economica");
    }

    private static void ocuparAsientosAleatorios(Vuelo vuelo, String categoria) {
        List<Asiento> categoriaAsientos = new ArrayList<>();
        for (Asiento asiento : vuelo.getAsientos()) {
            if (asiento.getCategoria().equalsIgnoreCase(categoria)) {
                categoriaAsientos.add(asiento);
            }
        }
        if (categoriaAsientos.isEmpty()) {
            return;
        }
        int cantidadOcupados = Math.min(5, Math.max(3, 3 + random.nextInt(3)));
        // No ocupar todos los asientos: dejar al menos uno disponible cuando sea posible
        int maxOcupables = Math.max(0, categoriaAsientos.size() - 1);
        cantidadOcupados = Math.min(cantidadOcupados, Math.max(0, maxOcupables));
        Collections.shuffle(categoriaAsientos);
        for (int i = 0; i < cantidadOcupados; i++) {
            categoriaAsientos.get(i).reservar();
        }
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
