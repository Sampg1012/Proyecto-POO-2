package com.agenciaviajes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
                fechaVuelo = fecha.isBefore(LocalDate.now()) ? LocalDate.now() : fecha;
            } else {
                fechaVuelo = LocalDate.now().plusDays(random.nextInt(60));
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

            aplicarReglasTarifa(vuelo);
            crearAsientos(vuelo);
            asignarEstadoVuelo(vuelo);
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

    private static void aplicarReglasTarifa(Vuelo vuelo) {
        if (vuelo == null) {
            return;
        }
        boolean internacional = vuelo instanceof VueloInternacional;
        vuelo.setDiasMinimosEstadia(internacional ? 5 : 3);
        vuelo.setDiasMaximosEstadia(internacional ? 180 : 90);
    }

    private static void asignarEstadoVuelo(Vuelo vuelo) {
        if (vuelo == null) {
            return;
        }
        LocalDate hoy = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(hoy, vuelo.getFecha());
        if (dias < 0) {
            vuelo.cambiarEstado(Vuelo.EstadoVuelo.FINALIZADO);
        } else if (dias == 0) {
            vuelo.cambiarEstado(random.nextBoolean() ? Vuelo.EstadoVuelo.EN_HORA : Vuelo.EstadoVuelo.RETRASADO);
        } else if (dias <= 2) {
            vuelo.cambiarEstado(random.nextBoolean() ? Vuelo.EstadoVuelo.PROGRAMADO : Vuelo.EstadoVuelo.EN_HORA);
        } else {
            vuelo.cambiarEstado(Vuelo.EstadoVuelo.PROGRAMADO);
        }
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
            int filasPrimera = 3;
            int filasEjecutiva = 5;
            int filasEconomica = 24;
            crearSeccionAsientos(vuelo, 1, filasPrimera, new char[]{'A', 'C', 'D', 'F'}, "Primera Clase");
            crearSeccionAsientos(vuelo, filasPrimera + 1, filasEjecutiva, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, "Ejecutiva");
            crearSeccionAsientos(vuelo, filasPrimera + filasEjecutiva + 1, filasEconomica, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, "Economica");
        } else {
            int filasPrimera = 3;
            int filasEjecutiva = 8;
            int filasEconomica = 30 + random.nextInt(10);
            crearSeccionAsientos(vuelo, 1, filasPrimera, new char[]{'A', 'C', 'D', 'F'}, "Primera Clase");
            crearSeccionAsientos(vuelo, filasPrimera + 1, filasEjecutiva, new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, "Ejecutiva");
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
        double factorDemanda = calcularFactorDemanda(vuelo);
        int cantidadOcupados = (int) Math.round(categoriaAsientos.size() * factorDemanda);
        cantidadOcupados = Math.max(0, Math.min(cantidadOcupados, categoriaAsientos.size() - 1));
        Collections.shuffle(categoriaAsientos);
        for (int i = 0; i < cantidadOcupados; i++) {
            categoriaAsientos.get(i).reservar();
        }
    }

    private static double calcularFactorDemanda(Vuelo vuelo) {
        if (vuelo == null) {
            return 0.12;
        }
        long diasHastaVuelo = ChronoUnit.DAYS.between(LocalDate.now(), vuelo.getFecha());
        if (diasHastaVuelo <= 0) {
            return 0.28;
        }
        if (diasHastaVuelo <= 3) {
            return 0.24;
        }
        if (diasHastaVuelo <= 7) {
            return 0.18;
        }
        return 0.10;
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
