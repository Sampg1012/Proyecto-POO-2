package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de inicio de sesion rediseñada con identidad visual ViajaYa.
 */
public class PanelLogin extends JPanel {

    private final JTextField txtLogin;
    private final JPasswordField txtContrasena;

    public PanelLogin(VentanaPrincipal ventana, AgenciaViajes agencia) {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO_CLARO);

        // Header
        add(Estilos.crearHeader("Iniciar Sesion"), BorderLayout.NORTH);

        // Tarjeta central
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilos.BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        tarjeta.setMaximumSize(new Dimension(420, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel icono = new JLabel("✈", SwingConstants.CENTER);
        icono.setFont(new Font("SansSerif", Font.PLAIN, 40));
        icono.setForeground(Estilos.AZUL_CIELO);
        gbc.gridx = 0; gbc.gridy = 0;
        tarjeta.add(icono, gbc);

        JLabel lblBienvenida = new JLabel("Bienvenido de nuevo", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBienvenida.setForeground(Estilos.TEXTO_OSCURO);
        gbc.gridy = 1;
        tarjeta.add(lblBienvenida, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        tarjeta.add(Estilos.labelCampo("Usuario:"), gbc);
        txtLogin = new JTextField(18);
        Estilos.estilizarCampo(txtLogin);
        gbc.gridx = 1;
        tarjeta.add(txtLogin, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        tarjeta.add(Estilos.labelCampo("Contrasena:"), gbc);
        txtContrasena = new JPasswordField(18);
        Estilos.estilizarCampo(txtContrasena);
        gbc.gridx = 1;
        tarjeta.add(txtContrasena, gbc);

        JButton btnIngresar = Estilos.botonPrincipal("Ingresar");
        JButton btnVolver = Estilos.botonVolver("Volver");
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 4;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnIngresar);
        panelBotones.add(btnVolver);
        tarjeta.add(panelBotones, gbc);

        JButton btnRegistro = Estilos.botonVolver("No tengo cuenta — Registrarme");
        gbc.gridy = 5;
        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRegistro.setBackground(Color.WHITE);
        panelRegistro.add(btnRegistro);
        tarjeta.add(panelRegistro, gbc);

        // Contenedor centrado
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilos.FONDO_CLARO);
        contenedor.add(tarjeta);
        add(contenedor, BorderLayout.CENTER);

        // Eventos
        btnIngresar.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            if (login.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el usuario y la contrasena.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (agencia.iniciarSesion(login, contrasena)) {
                txtLogin.setText("");
                txtContrasena.setText("");
                ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contrasena incorrectos.",
                        "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            txtLogin.setText("");
            txtContrasena.setText("");
            ventana.mostrarPanel(VentanaPrincipal.PANEL_BIENVENIDA);
        });

        btnRegistro.addActionListener(e -> {
            txtLogin.setText("");
            txtContrasena.setText("");
            ventana.mostrarPanel(VentanaPrincipal.PANEL_REGISTRO);
        });
    }
}