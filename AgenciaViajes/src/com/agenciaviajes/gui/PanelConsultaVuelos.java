package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.AgenciaViajes;
import com.agenciaviajes.modelo.CatalogoDatos;
import com.agenciaviajes.modelo.Vuelo;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelConsultaVuelos extends JPanel {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    private final AgenciaViajes agencia;

    private final JComboBox<String> cmbOrigen;
    private final JComboBox<String> cmbDestino;
    private final JTextField txtFechaIda;
    private final JTextField txtFechaVuelta;
    private final JTextField txtAerolinea;
    private final JCheckBox chkSoloDirectos;
    private final JComboBox<String> cmbOrden;
    private final JComboBox<String> cmbTipoViaje;
    private final JButton btnFechaIda;
    private final JButton btnFechaVuelta;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextArea areaEstado;

    public PanelConsultaVuelos(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.agencia = agencia;
        setLayout(new BorderLayout(12, 12));
        setBackground(Estilos.FONDO_CLARO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(Estilos.crearHeader("Consulta de Vuelos"), BorderLayout.NORTH);

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
        panelFiltros.add(Estilos.labelCampo("Tipo de viaje:"), gbc);
        cmbTipoViaje = new JComboBox<>(new String[]{"Solo ida", "Ida y vuelta"});
        Estilos.estilizarCombo(cmbTipoViaje);
        gbc.gridx = 1;
        panelFiltros.add(cmbTipoViaje, gbc);

        gbc.gridx = 2;
        panelFiltros.add(Estilos.labelCampo("Ida:"), gbc);
        txtFechaIda = new JTextField(12);
        txtFechaIda.setEditable(false);
        txtFechaIda.setText(LocalDate.now().format(FORMATO_FECHA));
        Estilos.estilizarCampo(txtFechaIda);
        btnFechaIda = new JButton("📅");
        btnFechaIda.setFocusPainted(false);
        JPanel panelIda = new JPanel(new BorderLayout(4, 0));
        panelIda.add(txtFechaIda, BorderLayout.CENTER);
        panelIda.add(btnFechaIda, BorderLayout.EAST);
        gbc.gridx = 3;
        panelFiltros.add(panelIda, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFiltros.add(Estilos.labelCampo("Regreso:"), gbc);
        txtFechaVuelta = new JTextField(12);
        txtFechaVuelta.setEditable(false);
        txtFechaVuelta.setEnabled(false);
        Estilos.estilizarCampo(txtFechaVuelta);
        btnFechaVuelta = new JButton("📅");
        btnFechaVuelta.setEnabled(false);
        btnFechaVuelta.setFocusPainted(false);
        JPanel panelVuelta = new JPanel(new BorderLayout(4, 0));
        panelVuelta.add(txtFechaVuelta, BorderLayout.CENTER);
        panelVuelta.add(btnFechaVuelta, BorderLayout.EAST);
        gbc.gridx = 1;
        panelFiltros.add(panelVuelta, gbc);

        gbc.gridx = 2;
        panelFiltros.add(Estilos.labelCampo("Aerolinea (opcional):"), gbc);
        txtAerolinea = new JTextField(12);
        Estilos.estilizarCampo(txtAerolinea);
        gbc.gridx = 3;
        panelFiltros.add(txtAerolinea, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
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
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panelFiltros.add(btnBuscar, gbc);

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

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        areaEstado = new JTextArea(3, 0);
        areaEstado.setEditable(false);
        areaEstado.setBorder(BorderFactory.createTitledBorder("Estado del vuelo seleccionado"));
        panelInferior.add(areaEstado, BorderLayout.CENTER);

        JButton btnCrearReserva = Estilos.botonPrincipal("Crear reserva con este vuelo");
        JButton btnVolver = new JButton("Volver al menu");
        JPanel panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonesInferior.add(btnCrearReserva);
        panelBotonesInferior.add(btnVolver);
        panelInferior.add(panelBotonesInferior, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarVuelos());
        btnFechaIda.addActionListener(e -> mostrarSelectorFecha(txtFechaIda, false));
        btnFechaVuelta.addActionListener(e -> mostrarSelectorFecha(txtFechaVuelta, true));
        cmbTipoViaje.addActionListener(e -> actualizarEstadoFechas());

        tabla.getSelectionModel().addListSelectionListener(e -> mostrarEstadoVueloSeleccionado());

        btnCrearReserva.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un vuelo de la tabla.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String idVuelo = (String) modeloTabla.getValueAt(fila, 0);
            ventana.mostrarPanel(VentanaPrincipal.PANEL_RESERVA, idVuelo);
        });

        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU));
    }

    private void actualizarEstadoFechas() {
        boolean idaYVuelta = "Ida y vuelta".equals(cmbTipoViaje.getSelectedItem());
        txtFechaVuelta.setEnabled(idaYVuelta);
        btnFechaVuelta.setEnabled(idaYVuelta);
        if (!idaYVuelta) {
            txtFechaVuelta.setText("");
        }
    }

    private void mostrarSelectorFecha(JTextField campoDestino, boolean esRetorno) {
        if (esRetorno && !"Ida y vuelta".equals(cmbTipoViaje.getSelectedItem())) {
            return;
        }
        LocalDate fechaBase = leerFechaDesdeCampo(txtFechaIda);
        if (esRetorno) {
            if (fechaBase == null) {
                JOptionPane.showMessageDialog(this, "Primero seleccione la fecha de ida.",
                        "Fecha de ida requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fechaBase = fechaBase.plusDays(1);
        } else {
            fechaBase = fechaBase == null ? LocalDate.now() : fechaBase;
        }

        JPopupMenu popup = new JPopupMenu();
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        JComboBox<Integer> cmbMes = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        JComboBox<Integer> cmbAnio = new JComboBox<>(new Integer[]{LocalDate.now().getYear(), LocalDate.now().getYear() + 1, LocalDate.now().getYear() + 2});
        JButton btnHoy = new JButton("Hoy");
        cmbMes.setSelectedItem(fechaBase.getMonthValue());
        cmbAnio.setSelectedItem(fechaBase.getYear());

        panelCabecera.add(cmbMes);
        panelCabecera.add(cmbAnio);
        panelCabecera.add(btnHoy);
        panel.add(panelCabecera, BorderLayout.NORTH);

        JPanel panelDias = new JPanel(new GridLayout(0, 7, 4, 4));
        String[] encabezados = {"D", "L", "M", "M", "J", "V", "S"};
        for (String encabezado : encabezados) {
            panelDias.add(new JLabel(encabezado, SwingConstants.CENTER));
        }

        Runnable refrescarDias = () -> {
            panelDias.removeAll();
            for (String encabezado : encabezados) {
                panelDias.add(new JLabel(encabezado, SwingConstants.CENTER));
            }
            int mes = (Integer) cmbMes.getSelectedItem();
            int anio = (Integer) cmbAnio.getSelectedItem();
            LocalDate primerDia = LocalDate.of(anio, mes, 1);
            int diaInicio = primerDia.getDayOfWeek().getValue() % 7;
            int diasMes = primerDia.lengthOfMonth();
            LocalDate hoy = LocalDate.now();
            LocalDate limiteInferior = hoy;
            LocalDate limiteSuperior = hoy.plusDays(330);
            LocalDate fechaIda = leerFechaDesdeCampo(txtFechaIda);
            if (esRetorno && fechaIda != null) {
                limiteInferior = fechaIda.plusDays(1);
                limiteSuperior = fechaIda.plusDays(365);
            }
            for (int i = 0; i < diaInicio; i++) {
                panelDias.add(new JLabel("", SwingConstants.CENTER));
            }
            for (int dia = 1; dia <= diasMes; dia++) {
                LocalDate fecha = LocalDate.of(anio, mes, dia);
                boolean valida = !fecha.isBefore(limiteInferior) && !fecha.isAfter(limiteSuperior);
                if (!esRetorno) {
                    valida = !fecha.isBefore(hoy) && !fecha.isAfter(hoy.plusDays(330));
                }
                JButton botonDia = new JButton(String.valueOf(dia));
                botonDia.setEnabled(valida);
                botonDia.addActionListener(ev -> {
                    campoDestino.setText(fecha.format(FORMATO_FECHA));
                    popup.setVisible(false);
                });
                panelDias.add(botonDia);
            }
            panelDias.revalidate();
            panelDias.repaint();
        };

        cmbMes.addActionListener(e -> refrescarDias.run());
        cmbAnio.addActionListener(e -> refrescarDias.run());
        btnHoy.addActionListener(e -> {
            cmbMes.setSelectedItem(LocalDate.now().getMonthValue());
            cmbAnio.setSelectedItem(LocalDate.now().getYear());
        });

        panel.add(new JScrollPane(panelDias), BorderLayout.CENTER);
        refrescarDias.run();
        popup.add(panel);
        popup.show(this, 0, this.getHeight());
    }

    private LocalDate leerFechaDesdeCampo(JTextField campo) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto, FORMATO_FECHA);
        } catch (Exception ex) {
            return null;
        }
    }

    private void buscarVuelos() {
        String origen = ((String) cmbOrigen.getSelectedItem()).trim();
        String destino = ((String) cmbDestino.getSelectedItem()).trim();
        String aerolinea = txtAerolinea.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar ciudad de origen y destino de la lista.",
                "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaIda = leerFechaDesdeCampo(txtFechaIda);
        if (fechaIda == null) {
            fechaIda = LocalDate.now();
            txtFechaIda.setText(fechaIda.format(FORMATO_FECHA));
        }
        if (fechaIda.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de ida debe ser hoy o posterior.",
                    "Fecha invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (fechaIda.isAfter(LocalDate.now().plusDays(330))) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de ida no puede estar a más de 330 días de distancia.",
                    "Fecha invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaVuelta = null;
        boolean idaYVuelta = "Ida y vuelta".equals(cmbTipoViaje.getSelectedItem());
        if (idaYVuelta) {
            fechaVuelta = leerFechaDesdeCampo(txtFechaVuelta);
            if (fechaVuelta == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una fecha de regreso.",
                        "Fecha incompleta", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!fechaVuelta.isAfter(fechaIda)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de regreso debe ser posterior a la fecha de ida.",
                        "Fecha invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fechaVuelta.isAfter(LocalDate.now().plusDays(330))) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de regreso no puede estar a más de 330 días de distancia.",
                        "Fecha invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            long diasViaje = ChronoUnit.DAYS.between(fechaIda, fechaVuelta);
            if (diasViaje > 365) {
                JOptionPane.showMessageDialog(this,
                        "La diferencia entre ida y regreso no puede superar 365 días.",
                        "Fecha invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        List<Vuelo> resultados;
        final LocalDate fechaIdaBusqueda = fechaIda;
        if (cmbOrden.getSelectedIndex() == 1) {
            resultados = agencia.consultarVuelosPorTarifa(origen, destino);
            resultados = resultados.stream().filter(v -> v.getFecha().equals(fechaIdaBusqueda)).toList();
            if (!aerolinea.isEmpty()) {
                resultados = resultados.stream()
                        .filter(v -> v.getAerolinea().getNombre().equalsIgnoreCase(aerolinea))
                        .toList();
            }
            if (chkSoloDirectos.isSelected()) {
                resultados = resultados.stream().filter(Vuelo::isEsDirecto).toList();
            }
            if (idaYVuelta && fechaVuelta != null) {
                int diasEstadia = (int) ChronoUnit.DAYS.between(fechaIda, fechaVuelta);
                resultados = resultados.stream().filter(v -> v.cumpleReglasEstadia(diasEstadia)).toList();
            }
        } else {
            resultados = agencia.consultarVuelosPorHorario(origen, destino, fechaIda,
                    aerolinea.isEmpty() ? null : aerolinea,
                    chkSoloDirectos.isSelected() ? Boolean.TRUE : null);
            if (idaYVuelta && fechaVuelta != null) {
                int diasEstadia = (int) ChronoUnit.DAYS.between(fechaIda, fechaVuelta);
                resultados = resultados.stream().filter(v -> v.cumpleReglasEstadia(diasEstadia)).toList();
            }
        }

        if (resultados.isEmpty()) {
            int generados = agencia.generarVuelosAleatorios(origen, destino, fechaIda,
                    aerolinea.isEmpty() ? null : aerolinea, 8).size();
            if (generados > 0) {
                JOptionPane.showMessageDialog(this,
                        "No había vuelos coincidentes; se generaron " + generados + " opciones aleatorias con esos filtros.",
                        "Vuelos generados", JOptionPane.INFORMATION_MESSAGE);
                resultados = agencia.consultarVuelosPorHorario(origen, destino, fechaIda,
                        aerolinea.isEmpty() ? null : aerolinea,
                        chkSoloDirectos.isSelected() ? Boolean.TRUE : null);
                if (idaYVuelta && fechaVuelta != null) {
                    int diasEstadia = (int) ChronoUnit.DAYS.between(fechaIda, fechaVuelta);
                    resultados = resultados.stream().filter(v -> v.cumpleReglasEstadia(diasEstadia)).toList();
                }
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

    public void actualizar() {
        modeloTabla.setRowCount(0);
        areaEstado.setText("");
    }
}