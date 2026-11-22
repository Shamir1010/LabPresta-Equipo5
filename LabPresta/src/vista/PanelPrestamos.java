package vista;

import datos.GestorEquipos;
import datos.GestorPrestamos;
import datos.GestorUsuarios;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Equipo;
import modelo.Prestamo;
import modelo.Usuario;

public class PanelPrestamos extends JPanel {

    private List<Prestamo> listaPrestamos;
    private List<Equipo> listaEquipos;
    private List<Usuario> listaUsuarios;
    private final Runnable onCambioEquipos;

    private JComboBox<String> cmbUsuarios;
    private JComboBox<String> cmbEquipos;
    private JTextField txtFechaPrestamo;
    private JLabel lblFechaLimitePreview;

    private JTextField txtFechaDevolucion;
    private DefaultTableModel modeloTabla;
    private JTable tablaPrestamos;

    public PanelPrestamos(Runnable onCambioEquipos) {
        this.onCambioEquipos = onCambioEquipos;
        construirInterfaz();
        recargarDatos();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(204, 204, 204));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Préstamo"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.anchor = GridBagConstraints.WEST;

        cmbUsuarios = new JComboBox<>();
        cmbEquipos = new JComboBox<>();
        txtFechaPrestamo = new JTextField(LocalDate.now().toString(), 12);
        lblFechaLimitePreview = new JLabel("-");

        cmbUsuarios.addActionListener(e -> actualizarVistaPreviaLimite());
        txtFechaPrestamo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                actualizarVistaPreviaLimite();
            }
        });

        JButton btnPrestar = new JButton("Registrar Préstamo");
        btnPrestar.addActionListener(e -> registrarPrestamo());

        agregarFila(panelFormulario, c, 0, "Usuario:", cmbUsuarios);
        agregarFila(panelFormulario, c, 1, "Equipo:", cmbEquipos);
        agregarFila(panelFormulario, c, 2, "Fecha préstamo (AAAA-MM-DD):", txtFechaPrestamo);
        agregarFila(panelFormulario, c, 3, "Fecha límite calculada:", lblFechaLimitePreview);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        panelFormulario.add(btnPrestar, c);

        modeloTabla = new DefaultTableModel(
                new String[]{"Equipo", "Usuario", "F. Préstamo", "F. Límite", "F. Devolución", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = new JTable(modeloTabla);

        JPanel panelDevolucion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelDevolucion.setOpaque(false);
        panelDevolucion.setBorder(BorderFactory.createTitledBorder("Registrar Devolución (seleccione un préstamo activo arriba)"));

        txtFechaDevolucion = new JTextField(LocalDate.now().toString(), 12);
        JButton btnDevolver = new JButton("Registrar Devolución");
        btnDevolver.addActionListener(e -> registrarDevolucion());

        panelDevolucion.add(new JLabel("Fecha devolución (AAAA-MM-DD):"));
        panelDevolucion.add(txtFechaDevolucion);
        panelDevolucion.add(btnDevolver);

        add(panelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);
        add(panelDevolucion, BorderLayout.SOUTH);
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta, java.awt.Component campo) {
        c.gridwidth = 1;
        c.gridy = fila;
        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(campo, c);
    }

    public final void recargarDatos() {
        listaPrestamos = GestorPrestamos.cargarPrestamos();
        listaEquipos = GestorEquipos.cargarEquipos();
        listaUsuarios = GestorUsuarios.cargarUsuarios();

        cmbUsuarios.removeAllItems();
        cmbUsuarios.addItem("Seleccione usuario...");
        for (Usuario u : listaUsuarios) {
            cmbUsuarios.addItem(u.getCedula() + " - " + u.getNombre() + " (" + u.getTipo() + ")");
        }

        cmbEquipos.removeAllItems();
        cmbEquipos.addItem("Seleccione equipo...");
        for (Equipo eq : listaEquipos) {
            cmbEquipos.addItem(eq.getCodigo() + " - " + eq.getTipo() + " [" + eq.getEstado() + "]");
        }

        actualizarVistaPreviaLimite();
        actualizarTabla();
    }

    private void actualizarVistaPreviaLimite() {
        int idx = cmbUsuarios.getSelectedIndex();
        if (idx <= 0 || idx > listaUsuarios.size()) {
            lblFechaLimitePreview.setText("Seleccione un usuario");
            return;
        }
        try {
            LocalDate fPrestamo = LocalDate.parse(txtFechaPrestamo.getText().trim());
            Usuario u = listaUsuarios.get(idx - 1);
            LocalDate fLimite = Prestamo.calcularFechaLimite(fPrestamo, u.getTipo());
            lblFechaLimitePreview.setText(fLimite.toString() + " (" + u.getTipo() + ")");
        } catch (DateTimeParseException ex) {
            lblFechaLimitePreview.setText("Formato de fecha inválido (use AAAA-MM-DD)");
        }
    }

    private void registrarPrestamo() {
        StringBuilder errores = new StringBuilder();

        int idxUsuario = cmbUsuarios.getSelectedIndex();
        int idxEquipo = cmbEquipos.getSelectedIndex();
        String fechaStr = txtFechaPrestamo.getText().trim();
        LocalDate fechaPrestamo = null;

        if (idxUsuario <= 0) {
            errores.append("- Debe seleccionar un usuario.\n");
        }
        if (idxEquipo <= 0) {
            errores.append("- Debe seleccionar un equipo.\n");
        }
        if (fechaStr.isEmpty()) {
            errores.append("- El campo 'Fecha préstamo' está vacío.\n");
        } else {
            try {
                fechaPrestamo = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException ex) {
                errores.append("- La fecha de préstamo debe tener formato AAAA-MM-DD.\n");
            }
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Corrija los siguientes errores antes de registrar:\n\n" + errores,
                    "Errores de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipo equipoSeleccionado = listaEquipos.get(idxEquipo - 1);
        if (!"Disponible".equalsIgnoreCase(equipoSeleccionado.getEstado())) {
            JOptionPane.showMessageDialog(this,
                    "El equipo '" + equipoSeleccionado.getCodigo() + "' no está Disponible (estado actual: " + equipoSeleccionado.getEstado() + ").",
                    "Equipo no disponible", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuarioSeleccionado = listaUsuarios.get(idxUsuario - 1);
        Prestamo nuevo = new Prestamo(
                equipoSeleccionado.getCodigo(),
                usuarioSeleccionado.getCedula(),
                usuarioSeleccionado.getTipo(),
                fechaPrestamo
        );

        equipoSeleccionado.setEstado("Prestado");
        listaPrestamos.add(nuevo);

        GestorPrestamos.guardarPrestamos(listaPrestamos);
        GestorEquipos.guardarEquipos(listaEquipos);

        if (onCambioEquipos != null) {
            onCambioEquipos.run();
        }

        recargarDatos();
        JOptionPane.showMessageDialog(this,
                "Préstamo registrado.\nFecha límite de devolución: " + nuevo.getFechaLimite());
    }

    private void registrarDevolucion() {
        int fila = tablaPrestamos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un préstamo de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prestamo p = listaPrestamos.get(fila);
        if (!p.estaActivo()) {
            JOptionPane.showMessageDialog(this,
                    "Este préstamo ya fue devuelto el " + p.getFechaDevolucion() + ".",
                    "Préstamo ya cerrado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fechaDevStr = txtFechaDevolucion.getText().trim();
        if (fechaDevStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El campo 'Fecha devolución' está vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaDev;
        try {
            fechaDev = LocalDate.parse(fechaDevStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de devolución debe tener formato AAAA-MM-DD.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fechaDev.isBefore(p.getFechaPrestamo())) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de devolución no puede ser anterior a la fecha de préstamo (" + p.getFechaPrestamo() + ").",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        p.setFechaDevolucion(fechaDev);

        for (Equipo eq : listaEquipos) {
            if (eq.getCodigo().equalsIgnoreCase(p.getCodigoEquipo())) {
                eq.setEstado("Disponible");
                break;
            }
        }

        GestorPrestamos.guardarPrestamos(listaPrestamos);
        GestorEquipos.guardarEquipos(listaEquipos);

        if (onCambioEquipos != null) {
            onCambioEquipos.run();
        }

        recargarDatos();
        String msgEstado = p.estaAtrasado() ? " (Devuelto con ATRASO)" : " (Devuelto a tiempo)";
        JOptionPane.showMessageDialog(this, "Devolución registrada" + msgEstado + ". Equipo nuevamente Disponible.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        LocalDate hoy = LocalDate.now();
        for (Prestamo p : listaPrestamos) {
            String estado;
            if (!p.estaActivo()) {
                estado = p.estaAtrasado() ? "Devuelto (Atrasado)" : "Devuelto";
            } else if (p.estaVencido(hoy)) {
                estado = "Activo (VENCIDO)";
            } else {
                estado = "Activo";
            }

            modeloTabla.addRow(new Object[]{
                p.getCodigoEquipo(),
                p.getCedulaUsuario(),
                p.getFechaPrestamo(),
                p.getFechaLimite(),
                p.estaActivo() ? "-" : p.getFechaDevolucion(),
                estado
            });
        }
    }
}