package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Pantalla que muestra las reservas del usuario actual, permite ver el
 * detalle de su itinerario y pasajeros, y cancelarlas (liberando asientos).
 */
public class PanelMisReservas extends JPanel {

    private final AgenciaViajes agencia;

    private final DefaultTableModel modeloReservas;
    private final JTable tablaReservas;
    private final JTextArea areaDetalle;

    public PanelMisReservas(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;

        setLayout(new BorderLayout(12, 12));
        setBackground(Estilos.FONDO_CLARO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(Estilos.crearHeader("Mis Reservas"), BorderLayout.NORTH);

        modeloReservas = new DefaultTableModel(
                new String[]{"ID Reserva", "Fecha", "Estado", "Vuelos", "Pasajeros", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReservas = new JTable(modeloReservas);
        Estilos.estilizarTabla(tablaReservas);

        areaDetalle = new JTextArea(10, 0);
        Estilos.estilizarAreaTexto(areaDetalle);
        areaDetalle.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilos.AZUL_CIELO, 1, true),
                "Detalle de la reserva seleccionada",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                Estilos.FUENTE_BOLD,
                Estilos.AZUL_CIELO));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tablaReservas), new JScrollPane(areaDetalle));
        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(BorderFactory.createLineBorder(Estilos.BORDE_SUAVE, 1, true));
        add(splitPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Estilos.FONDO_CLARO);
        JButton btnVerDetalle = Estilos.botonSecundario("Ver detalle");
        JButton btnCancelar = Estilos.botonVolver("Cancelar reserva");
        JButton btnVolver = Estilos.botonVolver("Volver al menu");
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        btnVerDetalle.addActionListener(e -> mostrarDetalle());
        btnCancelar.addActionListener(e -> cancelarReservaSeleccionada());
        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU));
    }

    /**
     * Recarga la tabla con las reservas del usuario actual.
     */
    public void actualizar() {
        modeloReservas.setRowCount(0);
        areaDetalle.setText("");
        if (agencia.getUsuarioActual() == null) {
            return;
        }
        for (Reserva r : agencia.getUsuarioActual().getReservas()) {
            modeloReservas.addRow(new Object[]{
                    r.getId(), r.getFechaReserva(), r.getEstado(),
                    r.getItinerario().getVuelos().size(),
                    r.getPasajeros().size(),
                    String.format("$%.2f", r.calcularTotal())
            });
        }
    }

    private Reserva obtenerReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String id = (String) modeloReservas.getValueAt(fila, 0);
        return agencia.getUsuarioActual().getReservas().stream()
                .filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    private void mostrarDetalle() {
        Reserva reserva = obtenerReservaSeleccionada();
        if (reserva == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Reserva: ").append(reserva.getId()).append("\n");
        sb.append("Fecha de reserva: ").append(reserva.getFechaReserva()).append("\n");
        sb.append("Estado: ").append(reserva.getEstado()).append("\n\n");

        sb.append("Itinerario:\n");
        if (reserva.getItinerario().estaVacio()) {
            sb.append("  (sin vuelos)\n");
        } else {
            for (Vuelo v : reserva.getItinerario().getVuelos()) {
                sb.append("  - ").append(v.toString()).append("\n");
            }
        }

        sb.append("\nPasajeros:\n");
        if (reserva.getPasajeros().isEmpty()) {
            sb.append("  (sin pasajeros)\n");
        } else {
            for (Pasajero p : reserva.getPasajeros()) {
                sb.append("  - ").append(p.toString()).append("\n");
            }
        }

        sb.append("\nAsientos asignados:\n");
        Map<String, Asiento> asignados = reserva.getAsientosAsignados();
        if (asignados.isEmpty()) {
            sb.append("  (sin asientos asignados)\n");
        } else {
            for (Pasajero p : reserva.getPasajeros()) {
                Asiento a = asignados.get(p.getId());
                if (a != null) {
                    sb.append("  - ").append(p.getNombre()).append(" -> Asiento ")
                            .append(a.getNumero()).append(" (").append(a.getCategoria()).append(")\n");
                }
            }
        }

        sb.append("\nValor informativo total: $").append(String.format("%.2f", reserva.calcularTotal()));

        areaDetalle.setText(sb.toString());
    }

    private void cancelarReservaSeleccionada() {
        Reserva reserva = obtenerReservaSeleccionada();
        if (reserva == null) return;

        if (reserva.getEstado() == Reserva.EstadoReserva.CANCELADA) {
            JOptionPane.showMessageDialog(this, "Esta reserva ya se encuentra cancelada.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de cancelar la reserva " + reserva.getId()
                        + "? Los asientos asociados quedaran disponibles nuevamente.",
                "Confirmar cancelacion", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean cancelada = agencia.cancelarReserva(reserva);
            if (cancelada) {
                JOptionPane.showMessageDialog(this, "Reserva cancelada con exito.",
                        "Reserva cancelada", JOptionPane.INFORMATION_MESSAGE);
                actualizar();
                areaDetalle.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible cancelar la reserva.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
