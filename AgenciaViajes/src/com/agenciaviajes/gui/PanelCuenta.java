package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de administracion de la cuenta del usuario actual.
 * Permite consultar y modificar los datos personales, o cancelar el registro.
 */
public class PanelCuenta extends JPanel {

    private final AgenciaViajes agencia;

    private final JLabel lblLogin;
    private final JTextField txtNombres;
    private final JTextField txtEmail;
    private final JPasswordField txtNuevaContrasena;

    public PanelCuenta(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;

        setLayout(new BorderLayout(0, 0));
        Estilos.aplicarFondo(this);

        add(Estilos.crearHeader("Mi Cuenta"), BorderLayout.NORTH);

        JPanel tarjeta = new JPanel(new GridBagLayout());
        Estilos.aplicarFondoSecundario(tarjeta);
        tarjeta.setMaximumSize(new Dimension(560, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = Estilos.labelTitulo("Actualice su informacion personal");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        tarjeta.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 1;
        tarjeta.add(Estilos.labelCampo("Usuario (login):"), gbc);
        lblLogin = new JLabel("");
        lblLogin.setFont(Estilos.FUENTE_NORMAL);
        lblLogin.setForeground(Estilos.TEXTO_OSCURO);
        gbc.gridx = 1;
        tarjeta.add(lblLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        tarjeta.add(Estilos.labelCampo("Nombres completos:"), gbc);
        txtNombres = new JTextField(20);
        Estilos.estilizarCampo(txtNombres);
        gbc.gridx = 1;
        tarjeta.add(txtNombres, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        tarjeta.add(Estilos.labelCampo("Correo electronico:"), gbc);
        txtEmail = new JTextField(20);
        Estilos.estilizarCampo(txtEmail);
        gbc.gridx = 1;
        tarjeta.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        tarjeta.add(Estilos.labelCampo("Nueva contrasena (opcional):"), gbc);
        txtNuevaContrasena = new JPasswordField(20);
        Estilos.estilizarCampo(txtNuevaContrasena);
        gbc.gridx = 1;
        tarjeta.add(txtNuevaContrasena, gbc);

        JButton btnGuardar = Estilos.botonPrincipal("Guardar cambios");
        JButton btnCancelarCuenta = Estilos.botonVolver("Cancelar mi registro");
        JButton btnVolver = Estilos.botonVolver("Volver al menu");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Estilos.FONDO_PANEL);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelarCuenta);
        panelBotones.add(btnVolver);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        tarjeta.add(panelBotones, gbc);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilos.FONDO_CLARO);
        contenedor.add(tarjeta);
        add(contenedor, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            String nombres = txtNombres.getText().trim();
            String email = txtEmail.getText().trim();
            String nuevaContrasena = new String(txtNuevaContrasena.getPassword());

            if (nombres.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nombres y correo no pueden estar vacios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean actualizado = agencia.actualizarUsuarioActual(nombres, email, nuevaContrasena);
            if (actualizado) {
                JOptionPane.showMessageDialog(this,
                        "Datos actualizados correctamente.",
                        "Cuenta actualizada", JOptionPane.INFORMATION_MESSAGE);
                txtNuevaContrasena.setText("");
                ventana.guardarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No fue posible actualizar los datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelarCuenta.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this, """
                                                                   Esta seguro de cancelar su registro? Se cancelaran tambien sus reservas activas.
                                                                   Esta accion no se puede deshacer.""",
                    "Confirmar cancelacion de registro", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean cancelado = agencia.cancelarRegistroUsuarioActual();
                if (cancelado) {
                    JOptionPane.showMessageDialog(this,
                            "Su registro ha sido cancelado.",
                            "Registro cancelado", JOptionPane.INFORMATION_MESSAGE);
                    ventana.guardarDatos();
                    ventana.mostrarPanel(VentanaPrincipal.PANEL_BIENVENIDA);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No fue posible cancelar el registro.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU));
    }

    /**
     * Carga los datos actuales del usuario en los campos del formulario.
     */
    public void actualizar() {
        if (agencia.getUsuarioActual() == null) {
            return;
        }
        lblLogin.setText(agencia.getUsuarioActual().getLogin());
        txtNombres.setText(agencia.getUsuarioActual().getNombres());
        txtEmail.setText(agencia.getUsuarioActual().getEmail());
        txtNuevaContrasena.setText("");
    }
}
            