package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import javax.swing.*;
import java.awt.*;

/**
 * Menu principal rediseñado con identidad visual ViajaYa.
 * Botones grandes con iconos para cada funcionalidad.
 */
public class PanelMenu extends JPanel {

    private final JLabel lblBienvenida;
    private final AgenciaViajes agencia;

    public PanelMenu(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO_CLARO);

        // Header
        add(Estilos.crearHeader("Menu Principal"), BorderLayout.NORTH);

        // Panel central
        JPanel centro = new JPanel(new BorderLayout(0, 20));
        centro.setBackground(Estilos.FONDO_CLARO);
        centro.setBorder(BorderFactory.createEmptyBorder(25, 40, 10, 40));

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblBienvenida.setForeground(Estilos.TEXTO_OSCURO);
        centro.add(lblBienvenida, BorderLayout.NORTH);

        // Grid de botones de menu
        JPanel gridMenu = new JPanel(new GridLayout(2, 2, 18, 18));
        gridMenu.setBackground(Estilos.FONDO_CLARO);

        JButton btnVuelos    = Estilos.botonMenu("Consultar Vuelos", "🔍");
        JButton btnReserva   = Estilos.botonMenu("Crear Reserva", "✈");
        JButton btnMisRes    = Estilos.botonMenu("Mis Reservas", "📋");
        JButton btnCuenta    = Estilos.botonMenu("Mi Cuenta", "👤");

        gridMenu.add(btnVuelos);
        gridMenu.add(btnReserva);
        gridMenu.add(btnMisRes);
        gridMenu.add(btnCuenta);

        centro.add(gridMenu, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // Panel inferior con cerrar sesion
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        sur.setBackground(Estilos.FONDO_CLARO);
        sur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Estilos.BORDE_SUAVE));

        JButton btnCerrar = Estilos.botonVolver("Cerrar Sesion");
        sur.add(btnCerrar);
        add(sur, BorderLayout.SOUTH);

        // Eventos
        btnVuelos.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_CONSULTA_VUELOS));
        btnReserva.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_RESERVA));
        btnMisRes.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MIS_RESERVAS));
        btnCuenta.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_CUENTA));

        btnCerrar.addActionListener(e -> {
            ventana.guardarDatos();
            agencia.cerrarSesion();
            ventana.mostrarPanel(VentanaPrincipal.PANEL_BIENVENIDA);
        });
    }

    public void actualizar() {
        if (agencia.getUsuarioActual() != null) {
            lblBienvenida.setText("Bienvenido, " + agencia.getUsuarioActual().getNombres() + " 👋");
        }
    }
}

