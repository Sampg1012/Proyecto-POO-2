package com.agenciaviajes.gui;

import javax.swing.*;
import java.awt.*;

public class PanelBienvenida extends JPanel {

    public PanelBienvenida(VentanaPrincipal ventana) {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO_CLARO);

        // Header con gradiente
        add(Estilos.crearHeader("Agencia de Viajes ViajaYa"), BorderLayout.NORTH);

        // Panel central
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Estilos.FONDO_CLARO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Subtitulo
        JLabel subtitulo = new JLabel("Su destino, nuestra mision", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitulo.setForeground(Estilos.TEXTO_GRIS);
        gbc.gridy = 0;
        centro.add(subtitulo, gbc);

        // Panel de servicios
        JPanel panelServicios = new JPanel(new GridLayout(5, 1, 0, 8));
        panelServicios.setBackground(Color.WHITE);
        panelServicios.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        String[] servicios = {
            "✈  Vuelos nacionales e internacionales por horario, tarifa o estado",
            "🗺  Reserva de vuelos directos o con escala con su propio itinerario",
            "👥  Gestion de pasajeros: adultos, niños y adultos mayores",
            "💺  Asientos en categoria economica, ejecutiva o primera clase",
            "📋  Administracion de reservas: consultar, revisar o cancelar"
        };

        for (String servicio : servicios) {
            JLabel lbl = new JLabel(servicio);
            lbl.setFont(Estilos.FUENTE_NORMAL);
            lbl.setForeground(Estilos.TEXTO_OSCURO);
            panelServicios.add(lbl);
        }

        gbc.gridy = 1;
        centro.add(panelServicios, gbc);

        add(centro, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        sur.setBackground(Estilos.FONDO_CLARO);
        sur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Estilos.BORDE_SUAVE));

        JButton btnLogin = Estilos.botonPrincipal("Iniciar Sesion");
        JButton btnRegistro = Estilos.botonSecundario("Registrarse");

        btnLogin.setPreferredSize(new Dimension(160, 42));
        btnRegistro.setPreferredSize(new Dimension(160, 42));

        btnLogin.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_LOGIN));
        btnRegistro.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_REGISTRO));

        sur.add(btnLogin);
        sur.add(btnRegistro);
        add(sur, BorderLayout.SOUTH);
    }
}