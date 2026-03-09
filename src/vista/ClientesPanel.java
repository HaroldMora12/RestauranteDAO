package vista;

import Dao.ClienteDAO;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientesPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;

    private int idSeleccionado = -1;

    public ClientesPanel() {

        setLayout(new BorderLayout(10,10));

        // ================= TABLA =================
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Email");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // ================= CONTENEDOR INFERIOR =================
        JPanel contenedorSur = new JPanel(new BorderLayout(10,10));

        // -------- FORMULARIO --------
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 5));

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        // -------- BOTONES --------
        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Colores
        btnAgregar.setBackground(new Color(10, 255, 66));
        btnAgregar.setForeground(Color.WHITE);

        btnActualizar.setBackground(new Color(255,193,7));
        btnActualizar.setForeground(Color.white);

        btnEliminar.setBackground(new Color(220,53,69));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        contenedorSur.add(panelFormulario, BorderLayout.CENTER);
        contenedorSur.add(panelBotones, BorderLayout.SOUTH);

        add(contenedorSur, BorderLayout.SOUTH);

        // ================= EVENTOS =================

        btnAgregar.addActionListener(e -> {

            if (txtNombre.getText().isEmpty() ||
                    txtTelefono.getText().isEmpty() ||
                    txtEmail.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            }

            Cliente c = new Cliente();
            c.setNombre(txtNombre.getText());
            c.setTelefono(txtTelefono.getText());
            c.setEmail(txtEmail.getText());

            new ClienteDAO().agregar(c);

            cargarDatos();
            limpiarCampos();
        });

        btnActualizar.addActionListener(e -> {

            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente primero");
                return;
            }

            Cliente c = new Cliente();
            c.setId(idSeleccionado);
            c.setNombre(txtNombre.getText());
            c.setTelefono(txtTelefono.getText());
            c.setEmail(txtEmail.getText());

            new ClienteDAO().actualizar(c);

            cargarDatos();
            limpiarCampos();
            idSeleccionado = -1;
        });

        btnEliminar.addActionListener(e -> {

            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente primero");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "¿Seguro que deseas eliminar este cliente?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                new ClienteDAO().eliminar(idSeleccionado);
                cargarDatos();
                limpiarCampos();
                idSeleccionado = -1;
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();

                idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtTelefono.setText(modelo.getValueAt(fila, 2).toString());
                txtEmail.setText(modelo.getValueAt(fila, 3).toString());
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {

        modelo.setRowCount(0);

        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.listar();

        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getTelefono(),
                    c.getEmail()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }
}
