package vista;

import datos.GestorUsuarios;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;

public class PanelUsuarios extends JPanel {

    private List<Usuario> listaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JComboBox<String> cmbTipo;

    public PanelUsuarios() {
        listaUsuarios = GestorUsuarios.cargarUsuarios();
        construirInterfaz();
        actualizarTabla();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(204, 204, 204));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 15, 8, 15);
        c.anchor = GridBagConstraints.WEST;

        txtCedula = new JTextField(15);
        txtNombre = new JTextField(15);
        txtTelefono = new JTextField(15);
        cmbTipo = new JComboBox<>(new String[]{"Seleccione tipo", "Estudiante", "Docente"});
        JButton btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.addActionListener(e -> registrarUsuario());

        agregarFila(formulario, c, 0, "Cédula:", txtCedula);
        agregarFila(formulario, c, 1, "Nombre completo:", txtNombre);
        agregarFila(formulario, c, 2, "Tipo:", cmbTipo);
        agregarFila(formulario, c, 3, "Teléfono:", txtTelefono);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        formulario.add(btnRegistrar, c);

        modeloTabla = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Tipo", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta, java.awt.Component campo) {
        c.gridwidth = 1;
        c.gridy = fila;
        c.gridx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(campo, c);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : listaUsuarios) {
            modeloTabla.addRow(new Object[]{
                u.getCedula(), u.getNombre(), u.getTipo(), u.getTelefono()
            });
        }
    }

        private String validar(String cedula, String nombre, String tipo, String telefono) {
        StringBuilder errores = new StringBuilder();

        if (cedula.isEmpty()) {
            errores.append("- El campo 'Cédula' está vacío.\n");
        } else {
            if (!cedula.matches("\\d+")) {
                errores.append("- La cédula solo puede contener números.\n");
            }
            for (Usuario u : listaUsuarios) {
                if (u.getCedula().equals(cedula)) {
                    errores.append("- La cédula '").append(cedula).append("' ya está registrada.\n");
                    break;
                }
            }
        }

        if (nombre.isEmpty()) {
            errores.append("- El campo 'Nombre completo' está vacío.\n");
        } else if (nombre.contains(",")) {
            errores.append("- El nombre no puede contener comas (,).\n");
        }

        if (tipo.isEmpty()) {
            errores.append("- Debe seleccionar el tipo de usuario (Estudiante o Docente).\n");
        }

        if (telefono.isEmpty()) {
            errores.append("- El campo 'Teléfono' está vacío.\n");
        } else if (!telefono.matches("\\d+")) {
            errores.append("- El teléfono solo puede contener números.\n");
        }

        return errores.toString();
    }

    private void registrarUsuario() {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String tipo = cmbTipo.getSelectedIndex() == 0 ? "" : cmbTipo.getSelectedItem().toString();

        String errores = validar(cedula, nombre, tipo, telefono);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Corrija los siguientes errores antes de registrar:\n\n" + errores,
                    "Errores de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        listaUsuarios.add(new Usuario(cedula, nombre, tipo, telefono));
        GestorUsuarios.guardarUsuarios(listaUsuarios);
        actualizarTabla();

        JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}