package com.agenciaviajes.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Catalogo de datos de referencia de la agencia: ciudades nacionales
 * (Colombia), destinos internacionales disponibles desde Colombia,
 * aerolineas, ciudades de escala mas comunes y rangos de tarifas e
 * impuestos usados por el {@link GeneradorVuelos} para crear vuelos
 * de forma aleatoria pero realista.
 *
 * Esta clase no contiene logica de negocio, solo informacion de
 * referencia (constantes). No requiere persistencia propia: a partir
 * de ella se generan los objetos Vuelo que si se guardan en el
 * archivo binario de la agencia.
 */
public final class CatalogoDatos {

    private static final Random RANDOM = new Random();

    static double generarTarifaReferencia(String origen, String destino) {
        String origenNormalizado = normalizar(origen);
        String destinoNormalizado = normalizar(destino);

        if (esCiudadNacional(origenNormalizado) && esCiudadNacional(destinoNormalizado)) {
            CategoriaNacional categoria = categoriaNacional(origenNormalizado, destinoNormalizado);
            return switch (categoria) {
                case CORTA -> RANDOM.nextInt(TARIFA_CORTA[0], TARIFA_CORTA[1] + 1);
                case MEDIA -> RANDOM.nextInt(TARIFA_MEDIA[0], TARIFA_MEDIA[1] + 1);
                case LARGA -> RANDOM.nextInt(TARIFA_LARGA[0], TARIFA_LARGA[1] + 1);
            };
        }

        CategoriaInternacional categoriaInternacional = CATEGORIA_DESTINO_INTERNACIONAL.getOrDefault(destinoNormalizado, CategoriaInternacional.REGIONAL);
        return switch (categoriaInternacional) {
            case REGIONAL -> RANDOM.nextInt(TARIFA_REGIONAL[0], TARIFA_REGIONAL[1] + 1);
            case NORTEAMERICA -> RANDOM.nextInt(TARIFA_NORTEAMERICA[0], TARIFA_NORTEAMERICA[1] + 1);
            case SURAMERICA_LEJANA -> RANDOM.nextInt(TARIFA_SURAMERICA_LEJANA[0], TARIFA_SURAMERICA_LEJANA[1] + 1);
            case EUROPA -> RANDOM.nextInt(TARIFA_EUROPA[0], TARIFA_EUROPA[1] + 1);
        };
    }

    static Aerolinea obtenerAerolineaAleatoria() {
        List<Aerolinea> aerolineas = esDestinoInternacional(normalizar(""))
                ? AEROLINEAS_INTERNACIONALES
                : AEROLINEAS_NACIONALES;
        return aerolineas.get(RANDOM.nextInt(aerolineas.size()));
    }

    static String obtenerEscalaAleatoria() {
        List<String> escalas = new ArrayList<>(ESCALAS_NACIONALES);
        escalas.addAll(ESCALAS_INTERNACIONALES);
        return escalas.get(RANDOM.nextInt(escalas.size()));
    }

    CatalogoDatos() {
    }

    /** Categoria de distancia para vuelos nacionales. */
    public enum CategoriaNacional {
        CORTA, MEDIA, LARGA
    }

    /** Categoria de region para vuelos internacionales (desde Colombia). */
    public enum CategoriaInternacional {
        REGIONAL, NORTEAMERICA, SURAMERICA_LEJANA, EUROPA
    }

    // -----------------------------------------------------------------
    // Ciudades nacionales (Colombia) -> codigo IATA del aeropuerto
    // -----------------------------------------------------------------
    public static final Map<String, String> CIUDADES_NACIONALES = mapaInmutable(
            "Bogota", "BOG",
            "Medellin", "MDE",
            "Cali", "CLO",
            "Cartagena", "CTG",
            "Barranquilla", "BAQ",
            "Santa Marta", "SMR",
            "Bucaramanga", "BGA",
            "Pereira", "PEI",
            "Armenia", "AXM",
            "Manizales", "MZL",
            "Ibague", "IBE",
            "Cucuta", "CUC",
            "San Andres", "ADZ",
            "Villavicencio", "VVC",
            "Leticia", "LET",
            "Pasto", "PSO",
            "Neiva", "NVA",
            "Monteria", "MTR",
            "Valledupar", "VUP",
            "Riohacha", "RCH"
    );

    // -----------------------------------------------------------------
    // Zonas geograficas (para estimar distancia/duracion/tarifa)
    // -----------------------------------------------------------------
    public static final Map<String, String> ZONA_CIUDAD = mapaInmutable(
            "Bogota", "ANDINA",
            "Medellin", "ANDINA",
            "Cali", "ANDINA",
            "Pereira", "ANDINA",
            "Armenia", "ANDINA",
            "Manizales", "ANDINA",
            "Ibague", "ANDINA",
            "Bucaramanga", "ANDINA",
            "Cucuta", "ANDINA",
            "Neiva", "ANDINA",
            "Pasto", "ANDINA",
            "Cartagena", "CARIBE",
            "Barranquilla", "CARIBE",
            "Santa Marta", "CARIBE",
            "Valledupar", "CARIBE",
            "Riohacha", "CARIBE",
            "Monteria", "CARIBE",
            "San Andres", "INSULAR",
            "Villavicencio", "ORINOQUIA",
            "Leticia", "AMAZONIA"
    );

    // -----------------------------------------------------------------
    // Destinos internacionales disponibles desde Colombia -> codigo IATA
    // -----------------------------------------------------------------
    public static final Map<String, String> DESTINOS_INTERNACIONALES = mapaInmutable(
            "Ciudad de Panama", "PTY",
            "Quito", "UIO",
            "Lima", "LIM",
            "Guayaquil", "GYE",
            "Miami", "MIA",
            "Nueva York", "JFK",
            "Orlando", "MCO",
            "Ciudad de Mexico", "MEX",
            "Cancun", "CUN",
            "Toronto", "YYZ",
            "Santiago de Chile", "SCL",
            "Buenos Aires", "EZE",
            "Sao Paulo", "GRU",
            "Madrid", "MAD",
            "Barcelona", "BCN",
            "Paris", "CDG",
            "Frankfurt", "FRA"
    );

    public static final Map<String, CategoriaInternacional> CATEGORIA_DESTINO_INTERNACIONAL = new LinkedHashMap<>();
    static {
        for (String c : new String[]{"Ciudad de Panama", "Quito", "Lima", "Guayaquil"}) {
            CATEGORIA_DESTINO_INTERNACIONAL.put(c, CategoriaInternacional.REGIONAL);
        }
        for (String c : new String[]{"Miami", "Nueva York", "Orlando", "Ciudad de Mexico", "Cancun", "Toronto"}) {
            CATEGORIA_DESTINO_INTERNACIONAL.put(c, CategoriaInternacional.NORTEAMERICA);
        }
        for (String c : new String[]{"Santiago de Chile", "Buenos Aires", "Sao Paulo"}) {
            CATEGORIA_DESTINO_INTERNACIONAL.put(c, CategoriaInternacional.SURAMERICA_LEJANA);
        }
        for (String c : new String[]{"Madrid", "Barcelona", "Paris", "Frankfurt"}) {
            CATEGORIA_DESTINO_INTERNACIONAL.put(c, CategoriaInternacional.EUROPA);
        }
    }

    // -----------------------------------------------------------------
    // Aerolineas
    // -----------------------------------------------------------------
    public static final List<Aerolinea> AEROLINEAS_NACIONALES = Arrays.asList(
            new Aerolinea("Avianca", "AV"),
            new Aerolinea("LATAM", "LA"),
            new Aerolinea("Wingo", "P5"),
            new Aerolinea("EasyFly", "VE"),
            new Aerolinea("Satena", "9R")
    );

    public static final List<Aerolinea> AEROLINEAS_INTERNACIONALES = Arrays.asList(
            new Aerolinea("Avianca", "AV"),
            new Aerolinea("LATAM", "LA"),
            new Aerolinea("Copa Airlines", "CM"),
            new Aerolinea("American Airlines", "AA"),
            new Aerolinea("Iberia", "IB"),
            new Aerolinea("Air France", "AF")
    );

    // -----------------------------------------------------------------
    // Ciudades de escala mas comunes
    // -----------------------------------------------------------------
    /** Escalas comunes para vuelos nacionales (grandes hubs domesticos). */
    public static final List<String> ESCALAS_NACIONALES = Arrays.asList(
            "Bogota", "Medellin", "Cali"
    );

    /** Escalas comunes para vuelos internacionales desde/hacia Colombia. */
    public static final List<String> ESCALAS_INTERNACIONALES = Arrays.asList(
            "Bogota", "Ciudad de Panama", "Miami", "Madrid"
    );

    // -----------------------------------------------------------------
    // Rangos de tarifa base (COP) y duracion (minutos) por categoria
    // -----------------------------------------------------------------
    // Vuelos nacionales
    public static final int[] TARIFA_CORTA = {110000, 220000};
    public static final int[] TARIFA_MEDIA = {180000, 350000};
    public static final int[] TARIFA_LARGA = {280000, 480000};

    public static final int[] DURACION_CORTA_MIN = {40, 75};
    public static final int[] DURACION_MEDIA_MIN = {70, 110};
    public static final int[] DURACION_LARGA_MIN = {110, 160};

    /** Impuesto/tasas de referencia para vuelos nacionales (porcentaje). */
    public static final double[] IMPUESTO_NACIONAL = {0.05, 0.10};

    // Vuelos internacionales
    public static final int[] TARIFA_REGIONAL = {550000, 1400000};
    public static final int[] TARIFA_NORTEAMERICA = {1200000, 3800000};
    public static final int[] TARIFA_SURAMERICA_LEJANA = {1500000, 4200000};
    public static final int[] TARIFA_EUROPA = {2500000, 6800000};

    public static final int[] DURACION_REGIONAL_MIN = {90, 180};
    public static final int[] DURACION_NORTEAMERICA_MIN = {210, 360};
    public static final int[] DURACION_SURAMERICA_LEJANA_MIN = {300, 540};
    public static final int[] DURACION_EUROPA_MIN = {540, 720};

    /** Impuesto/tasas de referencia para vuelos internacionales (porcentaje, incluye tasas aeroportuarias). */
    public static final double[] IMPUESTO_INTERNACIONAL = {0.12, 0.20};

    // -----------------------------------------------------------------
    // Utilidades
    // -----------------------------------------------------------------

    public static boolean esCiudadNacional(String ciudad) {
        return CIUDADES_NACIONALES.containsKey(normalizar(ciudad));
    }

    public static boolean esDestinoInternacional(String ciudad) {
        return DESTINOS_INTERNACIONALES.containsKey(normalizar(ciudad));
    }

    /**
     * Devuelve la categoria de distancia entre dos ciudades nacionales,
     * con base en si pertenecen a la misma zona geografica.
     */
    public static CategoriaNacional categoriaNacional(String origen, String destino) {
        String o = normalizar(origen);
        String d = normalizar(destino);
        if (o.equals("San Andres") || d.equals("San Andres") || o.equals("Leticia") || d.equals("Leticia")) {
            return CategoriaNacional.LARGA;
        }
        String zonaO = ZONA_CIUDAD.get(o);
        String zonaD = ZONA_CIUDAD.get(d);
        if (zonaO != null && zonaO.equals(zonaD)) {
            return CategoriaNacional.CORTA;
        }
        return CategoriaNacional.MEDIA;
    }

    /**
     * Normaliza el nombre de una ciudad para buscarla en los mapas del
     * catalogo (capitaliza cada palabra y recorta espacios).
     */
    public static String normalizar(String ciudad) {
        if (ciudad == null) {
            return "";
        }
        String texto = ciudad.trim().toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (String palabra : texto.split("\\s+")) {
            if (palabra.isEmpty()) continue;
            if (sb.length() > 0) sb.append(" ");
            sb.append(Character.toUpperCase(palabra.charAt(0))).append(palabra.substring(1));
        }
        return sb.toString();
    }

    /**
     * Devuelve la lista combinada de todas las ciudades/destinos disponibles
     * (nacionales + internacionales), util para llenar combos en la interfaz.
     */
    public static List<String> todasLasCiudades() {
        List<String> todas = new java.util.ArrayList<>();
        todas.addAll(CIUDADES_NACIONALES.keySet());
        todas.addAll(DESTINOS_INTERNACIONALES.keySet());
        return todas;
    }

    public static List<String> ciudadesNacionales() {
        return new java.util.ArrayList<>(CIUDADES_NACIONALES.keySet());
    }

    public static List<String> destinosInternacionales() {
        return new java.util.ArrayList<>(DESTINOS_INTERNACIONALES.keySet());
    }

    private static Map<String, String> mapaInmutable(String... entradas) {
        Map<String, String> mapa = new LinkedHashMap<>();
        for (int i = 0; i < entradas.length; i += 2) {
            mapa.put(entradas[i], entradas[i + 1]);
        }
        return mapa;
    }
}