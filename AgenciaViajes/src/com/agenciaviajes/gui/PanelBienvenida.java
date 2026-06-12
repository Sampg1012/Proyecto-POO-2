package com.agenciaviajes.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de bienvenida del sistema. Muestra un mensaje descriptivo
 * de los servicios de la agencia y permite ir a Iniciar Sesion o Registrarse.
 */
public class PanelBienvenida extends JPanel {

    public PanelBienvenida(VentanaPrincipal ventana) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("Bienvenido a la Agencia de Viajes ViajaYa", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        String mensaje = "<html><div style='text-align: center; font-size: 14px;'>"
                + "En ViajaYa le ofrecemos los siguientes servicios:<br><br>"
                + "&bull; Consulta de vuelos nacionales e internacionales por horario, tarifa o estado.<br>"
                + "&bull; Reserva de vuelos directos o con escala, construyendo su propio itinerario.<br>"
                + "&bull; Gestion de pasajeros: adultos, ninos y adultos mayores.<br>"
                + "&bull; Seleccion de asientos en categoria economica, ejecutiva o primera clase.<br>"
                + "&bull; Administracion de sus reservas: consultarlas, revisarlas o cancelarlas.<br><br>"
                + "Para comenzar, inicie sesion si ya esta registrado o cree una cuenta nueva."
                + "</div></html>";

        JLabel descripcion = new JLabel(mensaje);
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);
        add(descripcion, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnIniciarSesion = new JButton("Iniciar Sesion");
        JButton btnRegistrarse = new JButton("Registrarse");

        btnIniciarSesion.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_LOGIN));
        btnRegistrarse.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_REGISTRO));

        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnRegistrarse);

        add(panelBotones, BorderLayout.SOUTH);
    }
}