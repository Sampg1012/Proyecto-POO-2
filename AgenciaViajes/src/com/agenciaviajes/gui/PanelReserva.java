package com.agenciaviajes.gui;

import com.agenciaviajes.modelo.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelReserva extends JPanel {

    private final VentanaPrincipal ventana;
    private final AgenciaViajes agencia;
    private Reserva reservaActual;

    // ID de un vuelo que otra pantalla (Consulta de Vuelos) quiere
    // dejar listo en el campo de texto la proxima vez que se muestre
    // este panel. No se agrega automaticamente: el usuario decide
    // cuando darle clic a "Agregar al itinerario". Se consume una sola vez.
    private String idVueloPreseleccionado;

    private final JTextField txtIdVuelo;
    private final DefaultTableModel modeloItinerario;
    private final JTable tablaItinerario;

    private final JTextField txtPasId;
    private final JTextField txtPasNombre;
    private final JTextField txtPasEdad;
    private final JTextField txtPasContacto;
    private final JComboBox<String> cmbTipoPasajero;
    private final JTextField txtCampoExtra;
    private final JComboBox<String> cmbCampoExtra;
    private final JLabel lblCampoExtra;
    private final DefaultTableModel modeloPasajeros;
    private final JTable tablaPasajeros;

    private final JComboBox<String> cmbVueloAsiento;
    private final JComboBox<String> cmbPasajeroAsiento;
    private final JComboBox<String> cmbCategoriaAsiento;
    private final JComboBox<String> cmbAsientoDisponible;
    private final DefaultTableModel modeloAsientos;
    private final JTable tablaAsientos;

    private final JLabel lblTotal;

    public PanelReserva(VentanaPrincipal ventana, AgenciaViajes agencia) {
        this.ventana = ventana;
        this.agencia = agencia;

        setLayout(new BorderLayout(12, 12));
        setBackground(Estilos.FONDO_CLARO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(Estilos.crearHeader("Crear Reserva"), BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        Estilos.estilizarPestanas(pestañas);

        // ============== Pestaña 1: Itinerario ==============
        JPanel panelItinerario = new JPanel(new BorderLayout(10, 10));
        Estilos.aplicarFondoSecundario(panelItinerario);
        panelItinerario.setBackground(Estilos.FONDO_PANEL);

        JPanel panelAgregarVuelo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregarVuelo.setBackground(Estilos.FONDO_PANEL);
        panelAgregarVuelo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelAgregarVuelo.add(Estilos.labelCampo("ID del vuelo a agregar:"));
        txtIdVuelo = new JTextField(8);
        Estilos.estilizarCampo(txtIdVuelo);
        panelAgregarVuelo.add(txtIdVuelo);
        JButton btnAgregarVuelo = Estilos.botonSecundario("Agregar al itinerario");
        JButton btnQuitarVuelo = Estilos.botonVolver("Quitar vuelo seleccionado");
        panelAgregarVuelo.add(btnAgregarVuelo);
        panelAgregarVuelo.add(btnQuitarVuelo);

        modeloItinerario = new DefaultTableModel(
                new String[]{"ID", "Origen", "Destino", "Fecha", "Salida", "Llegada", "Tarifa final", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaItinerario = new JTable(modeloItinerario);
        Estilos.estilizarTabla(tablaItinerario);

        panelItinerario.add(panelAgregarVuelo, BorderLayout.NORTH);
        panelItinerario.add(new JScrollPane(tablaItinerario), BorderLayout.CENTER);
        pestañas.addTab("1. Itinerario", panelItinerario);

        // ============== Pestaña 2: Pasajeros ==============
        JPanel panelPasajeros = new JPanel(new BorderLayout(10, 10));
        Estilos.aplicarFondoSecundario(panelPasajeros);
        panelPasajeros.setBackground(Estilos.FONDO_PANEL);
        JPanel panelFormPasajero = new JPanel(new GridBagLayout());
        panelFormPasajero.setBackground(Estilos.FONDO_PANEL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormPasajero.add(Estilos.labelCampo("Tipo de pasajero:"), gbc);
        cmbTipoPasajero = new JComboBox<>(new String[]{"Adulto", "Nino", "Adulto Mayor"});
        Estilos.estilizarCombo(cmbTipoPasajero);
        gbc.gridx = 1;
        panelFormPasajero.add(cmbTipoPasajero, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormPasajero.add(Estilos.labelCampo("ID / Identificacion:"), gbc);
        txtPasId = new JTextField(10);
        Estilos.estilizarCampo(txtPasId);
        gbc.gridx = 1;
        panelFormPasajero.add(txtPasId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormPasajero.add(Estilos.labelCampo("Nombre:"), gbc);
        txtPasNombre = new JTextField(15);
        Estilos.estilizarCampo(txtPasNombre);
        gbc.gridx = 1;
        panelFormPasajero.add(txtPasNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormPasajero.add(Estilos.labelCampo("Edad:"), gbc);
        txtPasEdad = new JTextField(5);
        Estilos.estilizarCampo(txtPasEdad);
        gbc.gridx = 1;
        panelFormPasajero.add(txtPasEdad, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelFormPasajero.add(Estilos.labelCampo("Contacto:"), gbc);
        txtPasContacto = new JTextField(15);
        Estilos.estilizarCampo(txtPasContacto);
        gbc.gridx = 1;
        panelFormPasajero.add(txtPasContacto, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        lblCampoExtra = Estilos.labelCampo("Acompanante (Nino):");
        panelFormPasajero.add(lblCampoExtra, gbc);
        txtCampoExtra = new JTextField(15);
        Estilos.estilizarCampo(txtCampoExtra);
        cmbCampoExtra = new JComboBox<>(new String[]{"Si", "No"});
        Estilos.estilizarCombo(cmbCampoExtra);
        cmbCampoExtra.setVisible(false);
        gbc.gridx = 1;
        panelFormPasajero.add(txtCampoExtra, gbc);
        panelFormPasajero.add(cmbCampoExtra, gbc);

        JButton btnAgregarPasajero = Estilos.botonPrincipal("Agregar pasajero");
        JButton btnQuitarPasajero = Estilos.botonVolver("Quitar pasajero seleccionado");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel panelBotonesPasajero = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panelBotonesPasajero.setBackground(Estilos.FONDO_PANEL);
        panelBotonesPasajero.add(btnAgregarPasajero);
        panelBotonesPasajero.add(btnQuitarPasajero);
        panelFormPasajero.add(panelBotonesPasajero, gbc);

        modeloPasajeros = new DefaultTableModel(
                new String[]{"Tipo", "ID", "Nombre", "Edad", "Contacto", "Detalle"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPasajeros = new JTable(modeloPasajeros);
        Estilos.estilizarTabla(tablaPasajeros);

        panelPasajeros.add(panelFormPasajero, BorderLayout.NORTH);
        panelPasajeros.add(new JScrollPane(tablaPasajeros), BorderLayout.CENTER);
        pestañas.addTab("2. Pasajeros", panelPasajeros);

        cmbTipoPasajero.addActionListener(e -> actualizarCampoExtra());

        // ============== Pestaña 3: Asientos ==============
        JPanel panelAsientos = new JPanel(new BorderLayout(10, 10));
        Estilos.aplicarFondoSecundario(panelAsientos);
        panelAsientos.setBackground(Estilos.FONDO_PANEL);
        JPanel panelFormAsiento = new JPanel(new GridBagLayout());
        panelFormAsiento.setBackground(Estilos.FONDO_PANEL);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(8, 8, 8, 8);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        gbc2.gridx = 0; gbc2.gridy = 0;
        panelFormAsiento.add(Estilos.labelCampo("Vuelo:"), gbc2);
        cmbVueloAsiento = new JComboBox<>();
        Estilos.estilizarCombo(cmbVueloAsiento);
        gbc2.gridx = 1;
        panelFormAsiento.add(cmbVueloAsiento, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        panelFormAsiento.add(Estilos.labelCampo("Pasajero:"), gbc2);
        cmbPasajeroAsiento = new JComboBox<>();
        Estilos.estilizarCombo(cmbPasajeroAsiento);
        gbc2.gridx = 1;
        panelFormAsiento.add(cmbPasajeroAsiento, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2;
        panelFormAsiento.add(Estilos.labelCampo("Categoria de asiento:"), gbc2);
        cmbCategoriaAsiento = new JComboBox<>(new String[]{"Todas", "Economica", "Ejecutiva", "Primera Clase"});
        Estilos.estilizarCombo(cmbCategoriaAsiento);
        gbc2.gridx = 1;
        panelFormAsiento.add(cmbCategoriaAsiento, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 3;
        panelFormAsiento.add(Estilos.labelCampo("Asiento disponible:"), gbc2);
        cmbAsientoDisponible = new JComboBox<>();
        Estilos.estilizarCombo(cmbAsientoDisponible);
        gbc2.gridx = 1;
        panelFormAsiento.add(cmbAsientoDisponible, gbc2);

        JButton btnCargarAsientos = Estilos.botonSecundario("Cargar asientos disponibles");
        JButton btnAsignarAsiento = Estilos.botonPrincipal("Asignar asiento");
        gbc2.gridx = 0; gbc2.gridy = 4; gbc2.gridwidth = 2;
        JPanel panelBotonesAsiento = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panelBotonesAsiento.setBackground(Estilos.FONDO_PANEL);
        panelBotonesAsiento.add(btnCargarAsientos);
        panelBotonesAsiento.add(btnAsignarAsiento);
        panelFormAsiento.add(panelBotonesAsiento, gbc2);

        modeloAsientos = new DefaultTableModel(
                new String[]{"Pasajero", "Vuelo", "Asiento", "Categoria", "Recargo categoria", "Servicios incluidos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAsientos = new JTable(modeloAsientos);
        Estilos.estilizarTabla(tablaAsientos);

        panelAsientos.add(panelFormAsiento, BorderLayout.NORTH);
        panelAsientos.add(new JScrollPane(tablaAsientos), BorderLayout.CENTER);
        pestañas.addTab("3. Asientos", panelAsientos);

        add(pestañas, BorderLayout.CENTER);

        // ============== Panel inferior: total y confirmar ==============
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(Estilos.FONDO_CLARO);
        lblTotal = new JLabel("Valor informativo total: $0.00", SwingConstants.CENTER);
        lblTotal.setFont(Estilos.FUENTE_BOLD);
        lblTotal.setForeground(Estilos.AZUL_MARINO);
        panelInferior.add(lblTotal, BorderLayout.NORTH);

        JPanel panelBotonesFinales = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelBotonesFinales.setBackground(Estilos.FONDO_CLARO);
        JButton btnCalcularTotal = Estilos.botonSecundario("Calcular total");
        JButton btnConfirmar = Estilos.botonPrincipal("Confirmar reserva");
        JButton btnVolver = Estilos.botonVolver("Volver al menu");
        panelBotonesFinales.add(btnCalcularTotal);
        panelBotonesFinales.add(btnConfirmar);
        panelBotonesFinales.add(btnVolver);
        panelInferior.add(panelBotonesFinales, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // ============== Eventos ==============
        btnAgregarVuelo.addActionListener(e -> agregarVueloAlItinerario());
        btnQuitarVuelo.addActionListener(e -> quitarVueloDelItinerario());
        btnAgregarPasajero.addActionListener(e -> agregarPasajero());
        btnQuitarPasajero.addActionListener(e -> quitarPasajero());
        btnCargarAsientos.addActionListener(e -> cargarAsientosDisponibles());
        cmbCategoriaAsiento.addActionListener(e -> cargarAsientosDisponibles());
        btnAsignarAsiento.addActionListener(e -> asignarAsiento());
        btnCalcularTotal.addActionListener(e -> actualizarTotal());
        btnConfirmar.addActionListener(e -> confirmarReserva());
        btnVolver.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "Si vuelve al menu sin confirmar, la reserva en progreso se descartara. Desea continuar?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                descartarReservaEnProgreso();
                ventana.mostrarPanel(VentanaPrincipal.PANEL_MENU);
            }
        });

        actualizarCampoExtra();
    }

    /**
     * Permite que otra pantalla (por ejemplo, Consulta de Vuelos) deje listo
     * un ID de vuelo en el campo de texto la proxima vez que este panel se
     * muestre. No agrega el vuelo automaticamente: el usuario sigue teniendo
     * el control y debe darle clic a "Agregar al itinerario". Se consume
     * una sola vez dentro de actualizar().
     */
    public void preseleccionarVuelo(String idVuelo) {
        this.idVueloPreseleccionado = idVuelo;
    }

    /**
     * Se ejecuta cada vez que se muestra el panel: crea una nueva reserva en progreso.
     */
    public void actualizar() {
        if (agencia.getUsuarioActual() == null) {
            return;
        }
        reservaActual = agencia.crearReserva();
        modeloItinerario.setRowCount(0);
        modeloPasajeros.setRowCount(0);
        modeloAsientos.setRowCount(0);
        cmbVueloAsiento.removeAllItems();
        cmbPasajeroAsiento.removeAllItems();
        cmbCategoriaAsiento.setSelectedIndex(0);
        cmbAsientoDisponible.removeAllItems();
        lblTotal.setText("Valor informativo total: $0.00");
        txtPasId.setText("");
        txtPasNombre.setText("");
        txtPasEdad.setText("");
        txtPasContacto.setText("");
        txtCampoExtra.setText("");
        cmbCampoExtra.setSelectedIndex(0);

        if (idVueloPreseleccionado != null) {
            txtIdVuelo.setText(idVueloPreseleccionado);
            idVueloPreseleccionado = null;
        } else {
            txtIdVuelo.setText("");
        }
    }

    private void actualizarCampoExtra() {
        String tipo = (String) cmbTipoPasajero.getSelectedItem();
        if ("Nino".equals(tipo)) {
            lblCampoExtra.setText("Acompanante (Nino):");
            txtCampoExtra.setVisible(true);
            cmbCampoExtra.setVisible(false);
            txtCampoExtra.setEnabled(true);
            txtCampoExtra.setText("");
        } else if ("Adulto Mayor".equals(tipo)) {
            lblCampoExtra.setText("Requiere asistencia?");
            txtCampoExtra.setVisible(false);
            cmbCampoExtra.setVisible(true);
            cmbCampoExtra.setSelectedIndex(0);
        } else {
            lblCampoExtra.setText("No aplica:");
            txtCampoExtra.setVisible(false);
            cmbCampoExtra.setVisible(false);
            txtCampoExtra.setEnabled(false);
            txtCampoExtra.setText("");
        }
    }

    private void agregarVueloAlItinerario() {
        if (!validarReservaActiva()) return;

        String id = txtIdVuelo.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del vuelo.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Vuelo vuelo = agencia.buscarVueloPorId(id);
        if (vuelo == null) {
            JOptionPane.showMessageDialog(this, "No existe un vuelo con ese ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (reservaActual.getItinerario().getVuelos().contains(vuelo)) {
            JOptionPane.showMessageDialog(this, "Ese vuelo ya esta en el itinerario.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        reservaActual.getItinerario().agregarVuelo(vuelo);
        modeloItinerario.addRow(new Object[]{
                vuelo.getId(), vuelo.getOrigen(), vuelo.getDestino(),
                vuelo.getFecha(), vuelo.getHoraSalida(), vuelo.getHoraLlegada(),
                String.format("$%.2f", vuelo.calcularTarifaFinal()), vuelo.getTipoVuelo()
        });
        cmbVueloAsiento.addItem(vuelo.getId());
        txtIdVuelo.setText("");
        actualizarTotal();
    }

    private void quitarVueloDelItinerario() {
        int fila = tablaItinerario.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un vuelo de la tabla.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) modeloItinerario.getValueAt(fila, 0);
        Vuelo vuelo = agencia.buscarVueloPorId(id);
        if (vuelo != null) {
            reservaActual.getItinerario().eliminarVuelo(vuelo);
            cmbVueloAsiento.removeItem(id);
        }
        modeloItinerario.removeRow(fila);
        actualizarTotal();
    }

    private void agregarPasajero() {
        if (!validarReservaActiva()) return;

        String id = txtPasId.getText().trim();
        String nombre = txtPasNombre.getText().trim();
        String edadTexto = txtPasEdad.getText().trim();
        String contacto = txtPasContacto.getText().trim();
        String tipo = (String) cmbTipoPasajero.getSelectedItem();
        String extra;
        if ("Adulto Mayor".equals(tipo)) {
            extra = (String) cmbCampoExtra.getSelectedItem();
        } else {
            extra = txtCampoExtra.getText().trim();
        }

        if (id.isEmpty() || nombre.isEmpty() || edadTexto.isEmpty() || contacto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un numero entero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pasajero pasajero;
        String detalle;
        switch (tipo) {
            case "Nino":
                if (extra.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe indicar el acompanante del nino.",
                            "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                pasajero = new Nino(id, nombre, edad, contacto, extra);
                detalle = "Acompanante: " + extra;
                break;
            case "Adulto Mayor":
                boolean requiereAsistencia = "Si".equalsIgnoreCase(extra);
                pasajero = new AdultoMayor(id, nombre, edad, contacto, requiereAsistencia);
                detalle = "Asistencia: " + (requiereAsistencia ? "Si" : "No");
                break;
            default:
                pasajero = new Adulto(id, nombre, edad, contacto);
                detalle = "-";
                break;
        }

        boolean agregado = reservaActual.agregarPasajero(pasajero);
        if (!agregado) {
            JOptionPane.showMessageDialog(this,
                    "Los datos del pasajero no son validos para el tipo seleccionado "
                            + "(verifique la edad segun el tipo de pasajero).",
                    "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloPasajeros.addRow(new Object[]{pasajero.getTipo(), pasajero.getId(),
                pasajero.getNombre(), pasajero.getEdad(), pasajero.getContacto(), detalle});
        cmbPasajeroAsiento.addItem(pasajero.getId() + " - " + pasajero.getNombre());

        txtPasId.setText("");
        txtPasNombre.setText("");
        txtPasEdad.setText("");
        txtPasContacto.setText("");
        txtCampoExtra.setText("");
    }

    private void quitarPasajero() {
        int fila = tablaPasajeros.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pasajero de la tabla.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) modeloPasajeros.getValueAt(fila, 1);
        Pasajero pasajero = reservaActual.getPasajeros().stream()
                .filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (pasajero != null) {
            reservaActual.eliminarPasajero(pasajero);
            for (int i = 0; i < cmbPasajeroAsiento.getItemCount(); i++) {
                if (cmbPasajeroAsiento.getItemAt(i).startsWith(id + " - ")) {
                    cmbPasajeroAsiento.removeItemAt(i);
                    break;
                }
            }
        }
        modeloPasajeros.removeRow(fila);
        recargarTablaAsientos();
    }

    private void cargarAsientosDisponibles() {
        String idVuelo = (String) cmbVueloAsiento.getSelectedItem();
        if (idVuelo == null) {
            JOptionPane.showMessageDialog(this, "Agregue primero un vuelo al itinerario.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Vuelo vuelo = agencia.buscarVueloPorId(idVuelo);
        cmbAsientoDisponible.removeAllItems();
        if (vuelo == null) return;

        String categoriaFiltro = (String) cmbCategoriaAsiento.getSelectedItem();
        List<Asiento> disponibles = vuelo.obtenerAsientosDisponibles();
        if (categoriaFiltro != null && !categoriaFiltro.equals("Todas")) {
            disponibles = disponibles.stream()
                    .filter(a -> a.getCategoria().equalsIgnoreCase(categoriaFiltro))
                    .toList();
        }
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay asientos disponibles para esa categoria en este vuelo.",
                    "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        double tarifaVuelo = vuelo.calcularTarifaFinal();
        for (Asiento a : disponibles) {
            String precio = String.format("$%.0f", a.calcularPrecio(tarifaVuelo));
            cmbAsientoDisponible.addItem(a.getNumero() + " (" + a.getCategoria() + " - " + precio + ")");
        }
    }

    private void asignarAsiento() {
        String idVuelo = (String) cmbVueloAsiento.getSelectedItem();
        String pasajeroSel = (String) cmbPasajeroAsiento.getSelectedItem();
        String asientoSel = (String) cmbAsientoDisponible.getSelectedItem();

        if (idVuelo == null || pasajeroSel == null || asientoSel == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione vuelo, pasajero y asiento.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vuelo vuelo = agencia.buscarVueloPorId(idVuelo);
        String idPasajero = pasajeroSel.split(" - ")[0];
        Pasajero pasajero = reservaActual.getPasajeros().stream()
                .filter(p -> p.getId().equals(idPasajero)).findFirst().orElse(null);

        String numeroAsiento = asientoSel.split(" ")[0];
        Asiento asiento = vuelo.getAsientos().stream()
                .filter(a -> a.getNumero().equals(numeroAsiento)).findFirst().orElse(null);

        if (pasajero == null || asiento == null) {
            JOptionPane.showMessageDialog(this, "No se encontro el pasajero o el asiento.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean asignado;
        if (reservaActual.getAsientosAsignados().containsKey(pasajero.getId())) {
            asignado = reservaActual.reasignarAsientoAPasajero(pasajero, asiento);
            if (asignado) {
                JOptionPane.showMessageDialog(this,
                        "Asiento cambiado correctamente para el pasajero.",
                        "Asiento actualizado", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            asignado = reservaActual.asignarAsientoAPasajero(pasajero, asiento);
        }

        if (!asignado) {
            JOptionPane.showMessageDialog(this,
                    "No fue posible asignar el asiento (puede que ya este ocupado o ya tenga uno asignado).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        recargarTablaAsientos();
        cargarAsientosDisponibles();
        actualizarTotal();
    }

    private void recargarTablaAsientos() {
        modeloAsientos.setRowCount(0);
        if (reservaActual == null) {
            return;
        }
        for (Map.Entry<String, Asiento> entry : reservaActual.getAsientosAsignados().entrySet()) {
            Pasajero pasajero = reservaActual.getPasajeros().stream()
                    .filter(p -> p.getId().equals(entry.getKey()))
                    .findFirst()
                    .orElse(null);
            Asiento asiento = entry.getValue();
            if (pasajero == null || asiento == null) {
                continue;
            }
            Vuelo vuelo = agencia.buscarVueloPorId((String) cmbVueloAsiento.getSelectedItem());
            if (vuelo == null) {
                continue;
            }
            modeloAsientos.addRow(new Object[]{
                    pasajero.getNombre(), vuelo.getId(), asiento.getNumero(),
                    asiento.getCategoria(),
                    String.format("$%.0f", asiento.calcularRecargo(vuelo.calcularTarifaFinal())),
                    asiento.getServicios()
            });
        }
    }

    private void actualizarTotal() {
        if (reservaActual == null) {
            lblTotal.setText("Valor informativo total: $0.00");
            return;
        }
        lblTotal.setText(String.format("Valor informativo total: $%.2f", reservaActual.calcularTotal()));
    }

    private void confirmarReserva() {
        if (!validarReservaActiva()) return;

        if (reservaActual.getItinerario().estaVacio()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un vuelo al itinerario.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (reservaActual.getPasajeros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un pasajero.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean confirmada = reservaActual.confirmarReserva();
        if (confirmada) {
            actualizarTotal();
            JOptionPane.showMessageDialog(this,
                    "Reserva " + reservaActual.getId() + " confirmada con exito.\n"
                            + String.format("Valor informativo total: $%.2f", reservaActual.calcularTotal()),
                    "Reserva confirmada", JOptionPane.INFORMATION_MESSAGE);
            ventana.guardarDatos();
            reservaActual = null;
            ventana.mostrarPanel(VentanaPrincipal.PANEL_MIS_RESERVAS);
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible confirmar la reserva.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void descartarReservaEnProgreso() {
        if (reservaActual == null) return;
        reservaActual.cancelar();
        agencia.getReservas().remove(reservaActual);
        agencia.getUsuarioActual().getReservas().remove(reservaActual);
        reservaActual = null;
    }

    private boolean validarReservaActiva() {
        if (reservaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay una reserva en progreso. Vuelva al menu e intente de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}