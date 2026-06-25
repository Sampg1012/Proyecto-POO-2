package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import com.agenciaviajes.persistencia.GestorPersistencia;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public static final String PANEL_BIENVENIDA = "BIENVENIDA";
    public static final String PANEL_LOGIN = "LOGIN";
    public static final String PANEL_REGISTRO = "REGISTRO";
    public static final String PANEL_MENU = "MENU";
    public static final String PANEL_CONSULTA_VUELOS = "CONSULTA_VUELOS";
    public static final String PANEL_RESERVA = "RESERVA";
    public static final String PANEL_MIS_RESERVAS = "MIS_RESERVAS";
    public static final String PANEL_CUENTA = "CUENTA";

    private final AgenciaViajes agencia;
    private final CardLayout cardLayout;
    private final JPanel panelContenedor;

    private PanelBienvenida panelBienvenida;
    private PanelLogin panelLogin;
    private PanelRegistro panelRegistro;
    private PanelMenu panelMenu;
    private PanelConsultaVuelos panelConsultaVuelos;
    private PanelReserva panelReserva;
    private PanelMisReservas panelMisReservas;
    private PanelCuenta panelCuenta;

    public VentanaPrincipal(AgenciaViajes agencia) {
        this.agencia = agencia;

        setTitle("Sistema de Reservaciones - " + agencia.getNombre());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        panelBienvenida = new PanelBienvenida(this);
        panelLogin = new PanelLogin(this, agencia);
        panelRegistro = new PanelRegistro(this, agencia);
        panelMenu = new PanelMenu(this, agencia);
        panelConsultaVuelos = new PanelConsultaVuelos(this, agencia);
        panelReserva = new PanelReserva(this, agencia);
        panelMisReservas = new PanelMisReservas(this, agencia);
        panelCuenta = new PanelCuenta(this, agencia);

        panelContenedor.add(panelBienvenida, PANEL_BIENVENIDA);
        panelContenedor.add(panelLogin, PANEL_LOGIN);
        panelContenedor.add(panelRegistro, PANEL_REGISTRO);
        panelContenedor.add(panelMenu, PANEL_MENU);
        panelContenedor.add(panelConsultaVuelos, PANEL_CONSULTA_VUELOS);
        panelContenedor.add(panelReserva, PANEL_RESERVA);
        panelContenedor.add(panelMisReservas, PANEL_MIS_RESERVAS);
        panelContenedor.add(panelCuenta, PANEL_CUENTA);

        add(panelContenedor);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(VentanaPrincipal.this,
                        "Desea guardar los cambios antes de salir?",
                        "Salir", JOptionPane.YES_NO_CANCEL_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    GestorPersistencia.guardar(agencia);
                    dispose();
                    System.exit(0);
                } else if (opcion == JOptionPane.NO_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        mostrarPanel(PANEL_BIENVENIDA);
    }

    public void mostrarPanel(String nombrePanel) {
        mostrarPanel(nombrePanel, null);
    }

    public void mostrarPanel(String nombrePanel, String idVueloPreseleccionado) {
        switch (nombrePanel) {
            case PANEL_MENU:
                panelMenu.actualizar();
                break;
            case PANEL_CONSULTA_VUELOS:
                panelConsultaVuelos.actualizar();
                break;
            case PANEL_RESERVA:
                if (idVueloPreseleccionado != null) {
                    panelReserva.preseleccionarVuelo(idVueloPreseleccionado);
                }
                panelReserva.actualizar();
                break;
            case PANEL_MIS_RESERVAS:
                panelMisReservas.actualizar();
                break;
            case PANEL_CUENTA:
                panelCuenta.actualizar();
                break;
            default:
                break;
        }
        cardLayout.show(panelContenedor, nombrePanel);
    }

    public AgenciaViajes getAgencia() {
        return agencia;
    }

    public void guardarDatos() {
        GestorPersistencia.guardar(agencia);
    }
}
