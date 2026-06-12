package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import java.awt.*;
import javax.swing.*;

/**
 * Pantalla de inicio de sesion. Valida el login y la contrasena
 * del usuario contra los datos registrados en el sistema.
 */
public class PanelLogin extends JPanel {

    private final JTextField txtLogin;
    private final JPasswordField txtContrasena;

    public PanelLogin(VentanaPrincipal ventana, AgenciaViajes agencia) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Iniciar Sesion");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Usuario (login):"), gbc);

        txtLogin = new JTextField(20);
        gbc.gridx = 1;
        add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Contrasena:"), gbc);

        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnVolver = new JButton("Volver");
        JButton btnIrRegistro = new JButton("No tengo cuenta - Registrarme");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnIngresar);
        panelBotones.add(btnVolver);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelBotones, gbc);

        gbc.gridy = 4;
        add(btnIrRegistro, gbc);

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

        btnIrRegistro.addActionListener(e -> {
            txtLogin.setText("");
            txtContrasena.setText("");
            ventana.mostrarPanel(VentanaPrincipal.PANEL_REGISTRO);
        });
    }
}