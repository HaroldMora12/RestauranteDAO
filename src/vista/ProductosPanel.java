package vista;

import Dao.ProductoDAO;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductosPanel extends JPanel {

    JTable table;
    JTextField txtId, txtNombre, txtCategoria, txtPrecio;
    JComboBox<String> comboDisponibilidad;

    ProductoDAO dao = new ProductoDAO();

    public ProductosPanel() {

        setLayout(new BorderLayout(10,10));

        // ================= TABLA =================
        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ================= CONTENEDOR INFERIOR =================
        JPanel contenedorSur = new JPanel();
        contenedorSur.setLayout(new BorderLayout(10,10));

        // ================= FORMULARIO =================
        JPanel form = new JPanel(new GridLayout(5,2,10,5));

        txtId = new JTextField();
        txtId.setEnabled(false);

        txtNombre = new JTextField();
        txtCategoria = new JTextField();
        txtPrecio = new JTextField();

        comboDisponibilidad = new JComboBox<>();
        comboDisponibilidad.addItem("Disponible");
        comboDisponibilidad.addItem("No Disponible");

        form.add(new JLabel("ID"));
        form.add(txtId);

        form.add(new JLabel("Nombre"));
        form.add(txtNombre);

        form.add(new JLabel("Categoría"));
        form.add(txtCategoria);

        form.add(new JLabel("Precio"));
        form.add(txtPrecio);

        form.add(new JLabel("Disponibilidad"));
        form.add(comboDisponibilidad);

        // ================= BOTONES =================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        // Colores
        btnAgregar.setBackground(new Color(21, 255, 0));
        btnAgregar.setForeground(Color.white);

        btnActualizar.setBackground(new Color(255,193,7));
        btnActualizar.setForeground(Color.white);

        btnEliminar.setBackground(new Color(220,53,69));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        contenedorSur.add(form, BorderLayout.CENTER);
        contenedorSur.add(panelBotones, BorderLayout.SOUTH);

        add(contenedorSur, BorderLayout.SOUTH);

        cargarDatos();

        // ================= EVENTOS =================

        // AGREGAR
        btnAgregar.addActionListener(e -> {
            try {
                int disponibilidad = comboDisponibilidad.getSelectedIndex() == 0 ? 1 : 0;

                Producto p = new Producto(
                        txtNombre.getText(),
                        txtCategoria.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        disponibilidad
                );

                dao.agregar(p);
                cargarDatos();
                limpiar();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error en los datos");
            }
        });

        // ACTUALIZAR
        btnActualizar.addActionListener(e -> {
            try {
                int disponibilidad = comboDisponibilidad.getSelectedIndex() == 0 ? 1 : 0;

                Producto p = new Producto(
                        Integer.parseInt(txtId.getText()),
                        txtNombre.getText(),
                        txtCategoria.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        disponibilidad
                );

                dao.actualizar(p);
                cargarDatos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto válido");
            }
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            try {
                dao.eliminar(Integer.parseInt(txtId.getText()));
                cargarDatos();
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar");
            }
        });

        // CLICK TABLA
        table.getSelectionModel().addListSelectionListener(e -> {

            int fila = table.getSelectedRow();

            if (fila >= 0) {
                txtId.setText(table.getValueAt(fila, 0).toString());
                txtNombre.setText(table.getValueAt(fila, 1).toString());
                txtCategoria.setText(table.getValueAt(fila, 2).toString());
                txtPrecio.setText(table.getValueAt(fila, 3).toString());

                int disp = Integer.parseInt(table.getValueAt(fila, 4).toString());
                comboDisponibilidad.setSelectedIndex(disp == 1 ? 0 : 1);
            }
        });
    }

    private void cargarDatos() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nombre", "Categoría", "Precio", "Disponibilidad"});

        List<Producto> lista = dao.listar();

        for (Producto p : lista) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getCategoria(),
                    p.getPrecio(),
                    p.getDisponibilidad()
            });
        }

        table.setModel(model);
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        comboDisponibilidad.setSelectedIndex(0);
    }
}
