package com.agenciaviajes;

import com.agenciaviajes.gui.VentanaPrincipal;
import com.agenciaviajes.modelo.AgenciaViajes;
import com.agenciaviajes.persistencia.GestorPersistencia;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal que inicia la aplicacion de la Agencia de Viajes.
 */
public class App {

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Esta aplicación requiere un entorno gráfico para abrir la interfaz.");
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el look and feel por defecto
        }

        AgenciaViajes agencia = GestorPersistencia.cargar("Agencia de Viajes ViajaPYa");

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(agencia);
            ventana.setVisible(true);
        });
    }
}

