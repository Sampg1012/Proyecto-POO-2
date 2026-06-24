package com.agenciaviajes.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Clase de utilidades visuales para la interfaz grafica de ViajaYa.
 * Centraliza colores, fuentes y estilos para mantener consistencia.
 */
public class Estilos {

    // ---------------------------------------------------------------
    // Paleta de colores
    // ---------------------------------------------------------------
    public static final Color AZUL_MARINO     = new Color(11, 31, 58);    // #0B1F3A - headers
    public static final Color AZUL_CIELO      = new Color(46, 134, 222);  // #2E86DE - acento principal
    public static final Color DORADO          = new Color(240, 165, 0);   // #F0A500 - botones accion
    public static final Color DORADO_HOVER    = new Color(210, 140, 0);   // hover sobre dorado
    public static final Color FONDO_CLARO     = new Color(248, 250, 255); // #F8FAFF - fondo general
    public static final Color FONDO_PANEL     = new Color(232, 237, 245); // #E8EDF5 - paneles secundarios
    public static final Color TEXTO_OSCURO    = new Color(11, 31, 58);    // mismo que azul marino
    public static final Color TEXTO_CLARO     = Color.WHITE;
    public static final Color TEXTO_GRIS      = new Color(100, 116, 139);
    public static final Color BORDE_SUAVE     = new Color(200, 212, 230);
    public static final Color ROJO_ERROR      = new Color(220, 53, 69);
    public static final Color VERDE_EXITO     = new Color(40, 167, 69);

    // ---------------------------------------------------------------
    // Fuentes
    // ---------------------------------------------------------------
    public static final Font FUENTE_TITULO    = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FUENTE_MENU      = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font FUENTE_NORMAL    = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FUENTE_PEQUENA   = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FUENTE_BOLD      = new Font("SansSerif", Font.BOLD, 14);

    // ---------------------------------------------------------------
    // Metodos para crear componentes estilizados
    // ---------------------------------------------------------------

    /**
     * Crea el panel de header con gradiente azul marino -> azul cielo
     * y el nombre de la agencia en blanco centrado.
     */
    public static JPanel crearHeader(String titulo) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradiente = new GradientPaint(
                        0, 0, AZUL_MARINO,
                        getWidth(), 0, AZUL_CIELO
                );
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Icono avion
        JLabel icono = new JLabel("✈");
        icono.setFont(new Font("SansSerif", Font.PLAIN, 28));
        icono.setForeground(DORADO);
        header.add(icono, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(TEXTO_CLARO);
        header.add(lblTitulo, BorderLayout.CENTER);

        JLabel marca = new JLabel("ViajaYa");
        marca.setFont(new Font("SansSerif", Font.BOLD, 14));
        marca.setForeground(DORADO);
        header.add(marca, BorderLayout.EAST);

        return header;
    }

    /**
     * Aplica el fondo claro estandar a un JPanel.
     */
    public static void aplicarFondo(JPanel panel) {
        panel.setBackground(FONDO_CLARO);
    }

    /**
     * Crea un boton de accion principal (dorado con texto oscuro).
     */
    public static JButton botonPrincipal(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(DORADO);
        btn.setForeground(TEXTO_OSCURO);
        btn.setFont(FUENTE_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(DORADO_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(DORADO);
            }
        });
        return btn;
    }

    /**
     * Crea un boton secundario (azul cielo con texto blanco).
     */
    public static JButton botonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(AZUL_CIELO);
        btn.setForeground(TEXTO_CLARO);
        btn.setFont(FUENTE_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AZUL_MARINO);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(AZUL_CIELO);
            }
        });
        return btn;
    }

    /**
     * Crea un boton de volver/cancelar (contorno gris, sin relleno).
     * @param texto
     * @return 
     */
    public static JButton botonVolver(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(FONDO_CLARO);
        btn.setForeground(TEXTO_GRIS);
        btn.setFont(FUENTE_NORMAL);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btn.setOpaque(true);
        return btn;
    }

    /**
     * Crea un boton de menu grande (para el panel principal).
     * @param texto
     */
    public static JButton botonMenu(String texto, String icono) {
        JButton btn = new JButton("<html><center><span style='font-size:22px'>"
                + icono + "</span><br><span style='font-size:14px'>" + texto + "</span></center></html>");
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXTO_OSCURO);
        btn.setFont(FUENTE_MENU);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(240, 248, 255));
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AZUL_CIELO, 2, true),
                        BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });
        return btn;
    }

    /**
     * Estiliza un campo de texto o area de texto con bordes suaves y padding.
     */
    public static void estilizarCampo(JTextComponent campo) {
        campo.setFont(FUENTE_NORMAL);
        campo.setBackground(Color.WHITE);
        campo.setForeground(TEXTO_OSCURO);
        campo.setCaretColor(TEXTO_OSCURO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    /**
     * Estiliza un JComboBox con borde y tipografia del sistema.
     */
    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(FUENTE_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXTO_OSCURO);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        combo.setOpaque(true);
    }

    /**
     * Estiliza un JCheckBox simple para que combine con los paneles.
     */
    public static void estilizarCheckBox(JCheckBox casilla) {
        casilla.setFont(FUENTE_NORMAL);
        casilla.setForeground(TEXTO_OSCURO);
        casilla.setBackground(FONDO_CLARO);
    }

    /**
     * Estiliza un JTextArea como un panel de texto informativo.
     */
    public static void estilizarAreaTexto(JTextArea area) {
        area.setFont(FUENTE_NORMAL);
        area.setForeground(TEXTO_OSCURO);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    /**
     * Estiliza un JTabbedPane para que sea más suave y moderno.
     */
    public static void estilizarPestanas(JTabbedPane pestanas) {
        pestanas.setFont(FUENTE_BOLD);
        pestanas.setBackground(FONDO_CLARO);
        pestanas.setForeground(TEXTO_OSCURO);
        pestanas.setBorder(BorderFactory.createLineBorder(BORDE_SUAVE, 1, true));
    }

    /**
     * Crea un panel secundario con borde suave y fondo uniforme.
     */
    public static void aplicarFondoSecundario(JPanel panel) {
        panel.setBackground(FONDO_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
    }

    /**
     * Estiliza un JTable con colores corporativos.
     */
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_NORMAL);
        tabla.setRowHeight(30);
        tabla.setGridColor(FONDO_PANEL);
        tabla.setBackground(Color.WHITE);
        tabla.setForeground(TEXTO_OSCURO);
        tabla.setSelectionBackground(new Color(46, 134, 222, 60));
        tabla.setSelectionForeground(TEXTO_OSCURO);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        // Header de la tabla
        tabla.getTableHeader().setBackground(AZUL_MARINO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(FUENTE_BOLD);
        tabla.getTableHeader().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tabla.getTableHeader().setOpaque(true);

        // Asegurar que cada columna use un renderer de header opaco
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        headerRenderer.setBackground(AZUL_MARINO);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(FUENTE_BOLD);
        headerRenderer.setOpaque(true);
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    /**
     * Crea un JLabel de titulo de seccion.
     */
    public static JLabel labelTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FUENTE_SUBTITULO);
        lbl.setForeground(TEXTO_OSCURO);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        return lbl;
    }

    /**
     * Crea un JLabel de campo de formulario.
     */
    public static JLabel labelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_BOLD);
        lbl.setForeground(TEXTO_OSCURO);
        return lbl;
    }

    /**
     * Crea un borde con titulo estilizado para secciones.
     */
    public static Border bordeTitulado(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AZUL_CIELO, 1, true),
                titulo,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FUENTE_BOLD,
                AZUL_CIELO
        );
              
            }
        }