package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import com.agenciaviajes.modelo.Vuelo;
import com.agenciaviajes.modelo.CatalogoDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Pantalla de consulta de vuelos. Permite buscar vuelos entre dos ciudades
 * filtrando por fecha, aerolinea y preferencia de vuelo directo, y ordenar
 * los resultados por horario o por tarifa de referencia.
 */
public class PanelConsultaVuelos extends JPanel {

    private final AgenciaViajes agencia;

    private final JComboBox<String> cmbOrigen;
    private final JComboBox<String> cmbDestino;
    private final JTextField txtFecha;
    private final JTextField txtAerolinea;
    private final JCheckBox chkSoloDirectos;
    private final JComboBox<String> cmbOrden;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextArea areaEstado;

    public PanelConsultaVuelos(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;
        setLayout(new BorderLayout(12, 12));
        setBackground(Estilos.FONDO_CLARO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(Estilos.crearHeader("Consulta de Vuelos"), BorderLayout.NORTH);

        // ---- Panel de filtros ----
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        Estilos.aplicarFondoSecundario(panelFiltros);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFiltros.add(Estilos.labelCampo("Origen:"), gbc);
        cmbOrigen = new JComboBox<>();
        cmbOrigen.addItem("");
        for (String c : CatalogoDatos.todasLasCiudades()) cmbOrigen.addItem(c);
        cmbOrigen.setEditable(false);
        Estilos.estilizarCombo(cmbOrigen);
        gbc.gridx = 1;
        panelFiltros.add(cmbOrigen, gbc);

        gbc.gridx = 2;
        panelFiltros.add(Estilos.labelCampo("Destino:"), gbc);
        cmbDestino = new JComboBox<>();
        cmbDestino.addItem("");
        for (String c : CatalogoDatos.todasLasCiudades()) cmbDestino.addItem(c);
        cmbDestino.setEditable(false);
        Estilos.estilizarCombo(cmbDestino);
        gbc.gridx = 3;
        panelFiltros.add(cmbDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFiltros.add(Estilos.labelCampo("Fecha (AAAA-MM-DD, opcional):"), gbc);
        txtFecha = new JTextField(12);
        Estilos.estilizarCampo(txtFecha);
        gbc.gridx = 1;
        panelFiltros.add(txtFecha, gbc);

        gbc.gridx = 2;
        panelFiltros.add(Estilos.labelCampo("Aerolinea (opcional):"), gbc);
        txtAerolinea = new JTextField(12);
        Estilos.estilizarCampo(txtAerolinea);
        gbc.gridx = 3;
        panelFiltros.add(txtAerolinea, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        chkSoloDirectos = new JCheckBox("Solo vuelos directos");
        Estilos.estilizarCheckBox(chkSoloDirectos);
        gbc.gridwidth = 2;
        panelFiltros.add(chkSoloDirectos, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 2;
        panelFiltros.add(Estilos.labelCampo("Ordenar por:"), gbc);
        cmbOrden = new JComboBox<>(new String[]{"Horario", "Tarifa de referencia"});
        Estilos.estilizarCombo(cmbOrden);
        gbc.gridx = 3;
        panelFiltros.add(cmbOrden, gbc);

        JButton btnBuscar = Estilos.botonPrincipal("Buscar vuelos");
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panelFiltros.add(btnBuscar, gbc);

        // ---- Tabla de resultados ----
        String[] columnas = {"ID", "Origen", "Destino", "Fecha", "Salida", "Llegada",
                "Duracion", "Aerolinea", "Tipo", "Tarifa final", "Directo/Escala", "Estado", "Asientos disp."};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        // ---- Panel central con filtros arriba y tabla abajo ----
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // ---- Panel de estado de vuelo ----
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        areaEstado = new JTextArea(3, 0);
        areaEstado.setEditable(false);
        areaEstado.setBorder(BorderFactory.createTitledBorder("Estado del vuelo seleccionado"));
        panelInferior.add(areaEstado, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al menu");
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonesInferior.add(btnVolver);
        panelInferior.add(panelBotonesInferior, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // ---- Eventos ----
        btnBuscar.addActionListener(e -> buscarVuelos());

        tabla.getSelectionModel().addListSelectionListener(e -> mostrarEstadoVueloSeleccionado());

        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU));
    }

    private void buscarVuelos() {
        String origen = ((String) cmbOrigen.getSelectedItem()).trim();
        String destino = ((String) cmbDestino.getSelectedItem()).trim();
        String fechaTexto = txtFecha.getText().trim();
        String aerolinea = txtAerolinea.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar ciudad de origen y destino de la lista.",
                "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fecha = null;
        if (!fechaTexto.isEmpty()) {
            try {
                fecha = LocalDate.parse(fechaTexto);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Formato de fecha invalido. Use AAAA-MM-DD.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        List<Vuelo> resultados;
        if (cmbOrden.getSelectedIndex() == 1) {
            // Ordenar por tarifa de referencia
            resultados = agencia.consultarVuelosPorTarifa(origen, destino);
            if (fecha != null) {
                LocalDate f = fecha;
                resultados = resultados.stream()
                        .filter(v -> v.getFecha().equals(f))
                        .toList();
            }
            if (!aerolinea.isEmpty()) {
                resultados = resultados.stream()
                        .filter(v -> v.getAerolinea().getNombre().equalsIgnoreCase(aerolinea))
                        .toList();
            }
            if (chkSoloDirectos.isSelected()) {
                resultados = resultados.stream().filter(Vuelo::isEsDirecto).toList();
            }
        } else {
            resultados = agencia.consultarVuelosPorHorario(origen, destino, fecha,
                    aerolinea.isEmpty() ? null : aerolinea,
                    chkSoloDirectos.isSelected() ? Boolean.TRUE : null);
        }

        if (resultados.isEmpty()) {
            int generados = agencia.generarVuelosAleatorios(origen, destino, fecha,
                    aerolinea.isEmpty() ? null : aerolinea, 8).size();
            if (generados > 0) {
                JOptionPane.showMessageDialog(this,
                        "No había vuelos coincidentes; se generaron " + generados + " opciones aleatorias con esos filtros.",
                        "Vuelos generados", JOptionPane.INFORMATION_MESSAGE);
                resultados = agencia.consultarVuelosPorHorario(origen, destino, fecha,
                        aerolinea.isEmpty() ? null : aerolinea,
                        chkSoloDirectos.isSelected() ? Boolean.TRUE : null);
            }
        }

        modeloTabla.setRowCount(0);
        for (Vuelo v : resultados) {
            modeloTabla.addRow(new Object[]{
                    v.getId(),
                    v.getOrigen(),
                    v.getDestino(),
                    v.getFecha().toString(),
                    v.getHoraSalida().toString(),
                    v.getHoraLlegada().toString(),
                    formatearDuracion(v),
                    v.getAerolinea().getNombre(),
                    v.getTipoVuelo(),
                    String.format("$%.2f", v.calcularTarifaFinal()),
                    v.isEsDirecto() ? "Directo" : "Con escala (" + String.join(", ", v.getCiudadesEscala()) + ")",
                    v.getEstado(),
                    v.obtenerAsientosDisponibles().size()
            });
        }

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron vuelos para los criterios indicados.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
        areaEstado.setText("");
    }

    private String formatearDuracion(Vuelo v) {
        long minutos = v.calcularDuracion().toMinutes();
        return (minutos / 60) + "h " + (minutos % 60) + "m";
    }

    private void mostrarEstadoVueloSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            areaEstado.setText("");
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        Vuelo v = agencia.buscarVueloPorId(id);
        if (v == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Vuelo ").append(v.getId()).append(": Estado actual -> ").append(v.getEstado()).append(". ");
        sb.append("Asientos disponibles: ").append(v.obtenerAsientosDisponibles().size())
                .append(" de ").append(v.getAsientos().size()).append(" totales. ");
        sb.append(v.consultarDisponibilidad() ? "El vuelo tiene cupos disponibles." : "El vuelo se encuentra agotado.");
        areaEstado.setText(sb.toString());
    }

    /**
     * Refresca la tabla al mostrar el panel (limpia resultados anteriores).
     */
    public void actualizar() {
        modeloTabla.setRowCount(0);
        areaEstado.setText("");
    }
}
