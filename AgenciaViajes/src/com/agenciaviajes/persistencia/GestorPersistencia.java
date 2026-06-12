package com.agenciaviajes.persistencia;

import com.agenciaviajes.modelo.AgenciaViajes;

import java.io.*;

/**
 * Clase encargada de la persistencia de la informacion del sistema.
 * Utiliza serializacion de objetos para guardar y recuperar el estado
 * completo de la agencia (usuarios, vuelos, aerolineas y reservas)
 * en un archivo local.
 */
public class GestorPersistencia {

    private static final String ARCHIVO_DATOS = "data/agencia.dat";

    /**
     * Guarda el objeto AgenciaViajes en un archivo binario.
     */
    public static void guardar(AgenciaViajes agencia) {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(agencia);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Carga el objeto AgenciaViajes desde el archivo binario.
     * Si el archivo no existe o hay un error, devuelve una nueva agencia
     * con datos de demostracion.
     */
    public static AgenciaViajes cargar(String nombreAgencia) {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            AgenciaViajes nueva = new AgenciaViajes(nombreAgencia);
            nueva.cargarDatosDemo();
            return nueva;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_DATOS))) {
            return (AgenciaViajes) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
            AgenciaViajes nueva = new AgenciaViajes(nombreAgencia);
            nueva.cargarDatosDemo();
            return nueva;
        }
    }
}