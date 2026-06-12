package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;

import javax.swing.*;
import java.awt.*;

/**
 * Menu principal mostrado luego de iniciar sesion. Permite navegar a las
 * distintas funcionalidades: consulta de vuelos, reservas, mis reservas
 * y administracion de la cuenta.
 */
public class PanelMenu extends JPanel {

    private final JLabel lblBienvenida;
    private final AgenciaViajes agencia;

    public PanelMenu(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lblBienvenida, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton btnConsultarVuelos = new JButton("Consultar Vuelos");
        JButton btnCrearReserva = new JButton("Crear Reserva");
        JButton btnMisReservas = new JButton("Mis Reservas");
        JButton btnCuenta = new JButton("Mi Cuenta");

        for (JButton b : new JButton[]{btnConsultarVuelos, btnCrearReserva, btnMisReservas, btnCuenta}) {
            b.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }

        panelOpciones.add(btnConsultarVuelos);
        panelOpciones.add(btnCrearReserva);
        panelOpciones.add(btnMisReservas);
        panelOpciones.add(btnCuenta);

        add(panelOpciones, BorderLayout.CENTER);

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInferior.add(btnCerrarSesion);
        add(panelInferior, BorderLayout.SOUTH);

        btnConsultarVuelos.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_CONSULTA_VUELOS));
        btnCrearReserva.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_RESERVA));
        btnMisReservas.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MIS_RESERVAS));
        btnCuenta.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_CUENTA));

        btnCerrarSesion.addActionListener(e -> {
            ventana.guardarDatos();
            agencia.cerrarSesion();
            ventana.mostrarPanel(VentanaPrincipal.PANEL_BIENVENIDA);
        });
    }

    /**
     * Actualiza el mensaje de bienvenida con el nombre del usuario actual.
     */
    public void actualizar() {
        if (agencia.getUsuarioActual() != null) {
            lblBienvenida.setText("Bienvenido, " + agencia.getUsuarioActual().getNombres());
        }
    }
}
