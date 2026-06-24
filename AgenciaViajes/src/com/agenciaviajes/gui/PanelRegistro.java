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
        setLayout(new BorderLayout(0, 0));
        Estilos.aplicarFondo(this);

        add(Estilos.crearHeader("Registro de Usuario"), BorderLayout.NORTH);

        JPanel tarjeta = new JPanel(new GridBagLayout());
        Estilos.aplicarFondoSecundario(tarjeta);
        tarjeta.setMaximumSize(new Dimension(560, 460));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = Estilos.labelTitulo("Complete sus datos para crear su cuenta");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        tarjeta.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        tarjeta.add(Estilos.labelCampo("Nombres completos:"), gbc);
        txtNombres = new JTextField(20);
        Estilos.estilizarCampo(txtNombres);
        gbc.gridx = 1;
        tarjeta.add(txtNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        tarjeta.add(Estilos.labelCampo("Correo electronico:"), gbc);
        txtEmail = new JTextField(20);
        Estilos.estilizarCampo(txtEmail);
        gbc.gridx = 1;
        tarjeta.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        tarjeta.add(Estilos.labelCampo("Usuario (login):"), gbc);
        txtLogin = new JTextField(20);
        Estilos.estilizarCampo(txtLogin);
        gbc.gridx = 1;
        tarjeta.add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        tarjeta.add(Estilos.labelCampo("Contrasena:"), gbc);
        txtContrasena = new JPasswordField(20);
        Estilos.estilizarCampo(txtContrasena);
        gbc.gridx = 1;
        tarjeta.add(txtContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        tarjeta.add(Estilos.labelCampo("Confirmar contrasena:"), gbc);
        txtConfirmarContrasena = new JPasswordField(20);
        Estilos.estilizarCampo(txtConfirmarContrasena);
        gbc.gridx = 1;
        tarjeta.add(txtConfirmarContrasena, gbc);

        JButton btnRegistrar = Estilos.botonPrincipal("Registrarse");
        JButton btnVolver = Estilos.botonVolver("Volver");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Estilos.FONDO_PANEL);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        tarjeta.add(panelBotones, gbc);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilos.FONDO_CLARO);
        contenedor.add(tarjeta);
        add(contenedor, BorderLayout.CENTER);

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