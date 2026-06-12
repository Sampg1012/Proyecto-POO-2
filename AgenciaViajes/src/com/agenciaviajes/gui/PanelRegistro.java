package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de registro de un nuevo usuario en el sistema.
 */
public class PanelRegistro extends JPanel {

    private final JTextField txtNombres;
    private final JTextField txtEmail;
    private final JTextField txtLogin;
    private final JPasswordField txtContrasena;
    private final JPasswordField txtConfirmarContrasena;

    public PanelRegistro(VentanaPrincipal ventana, AgenciaViajes agencia) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Registro de Usuario");
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
        add(new JLabel("Nombres completos:"), gbc);
        txtNombres = new JTextField(20);
        gbc.gridx = 1;
        add(txtNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Correo electronico:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Usuario (login):"), gbc);
        txtLogin = new JTextField(20);
        gbc.gridx = 1;
        add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Contrasena:"), gbc);
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Confirmar contrasena:"), gbc);
        txtConfirmarContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtConfirmarContrasena, gbc);

        JButton btnRegistrar = new JButton("Registrarse");
        JButton btnVolver = new JButton("Volver");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelBotones, gbc);

        btnRegistrar.addActionListener(e -> {
            String nombres = txtNombres.getText().trim();
            String email = txtEmail.getText().trim();
            String login = txtLogin.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String confirmacion = new String(txtConfirmarContrasena.getPassword());

            if (nombres.isEmpty() || email.isEmpty() || login.isEmpty()
                    || contrasena.isEmpty() || confirmacion.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!contrasena.equals(confirmacion)) {
                JOptionPane.showMessageDialog(this,
                        "Las contrasenas no coinciden.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean exito = agencia.registrarse(nombres, email, login, contrasena);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Registro exitoso. Ahora puede iniciar sesion.",
                        "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                ventana.guardarDatos();
                ventana.mostrarPanel(VentanaPrincipal.PANEL_LOGIN);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El nombre de usuario ya esta en uso. Elija otro.",
                        "Error de registro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarCampos();
            ventana.mostrarPanel(VentanaPrincipal.PANEL_BIENVENIDA);
        });
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtEmail.setText("");
        txtLogin.setText("");
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
    }
}