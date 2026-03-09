package vista;

import Dao.*;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenesPanel extends JPanel {

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Producto> comboProducto;
    private JTextField txtCantidad;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    private String monedaActual = "COP";

    private List<OrdenProducto> listaDetalle = new ArrayList<>();

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private OrdenDAO ordenDAO = new OrdenDAO();
    private OrdenProductoDAO ordenProductoDAO = new OrdenProductoDAO();

    public OrdenesPanel() {

        setLayout(new BorderLayout(10,10));
        setBackground(Color.WHITE);

        // ================= PANEL SUPERIOR =================
        JPanel panelSuperior = new JPanel(new GridLayout(2,5,10,5));
        panelSuperior.setBackground(Color.WHITE);

        comboCliente = new JComboBox<>();
        comboProducto = new JComboBox<>();
        txtCantidad = new JTextField();

        panelSuperior.add(new JLabel("Cliente:"));
        panelSuperior.add(comboCliente);

        panelSuperior.add(new JLabel("Producto:"));
        panelSuperior.add(comboProducto);

        panelSuperior.add(new JLabel("Cantidad:"));
        panelSuperior.add(txtCantidad);

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");

        estilizarBoton(btnAgregar, new Color(40, 167, 69));
        estilizarBoton(btnEliminar, new Color(255, 140, 0));

        panelSuperior.add(btnAgregar);
        panelSuperior.add(btnEliminar);

        add(panelSuperior, BorderLayout.NORTH);

        // ================= TABLA =================
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Subtotal");

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ================= PANEL INFERIOR =================
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);

        lblTotal = new JLabel("Total: $0 COP");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        // ====== PANEL DE CONVERSION ======
        JPanel panelConversion = new JPanel();
        panelConversion.setBackground(Color.WHITE);

        JComboBox<String> comboMoneda = new JComboBox<>(new String[]{"USD", "EUR", "MXN", "BRL"});
        JButton btnConvertir = new JButton("Convertir");
        estilizarBoton(btnConvertir, new Color(108, 117, 125));

        panelConversion.add(new JLabel("Convertir a: "));
        panelConversion.add(comboMoneda);
        panelConversion.add(btnConvertir);

        // ====== BOTONES ORDEN ======
        JButton btnGuardar = new JButton("Guardar Orden");
        JButton btnCancelar = new JButton("Cancelar Orden");

        estilizarBoton(btnGuardar, new Color(0, 123, 255));
        estilizarBoton(btnCancelar, new Color(220, 53, 69));

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelInferior.add(lblTotal, BorderLayout.WEST);
        panelInferior.add(panelConversion, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        cargarClientes();
        cargarProductos();

        // ================= EVENTO CONVERTIR =================
        btnConvertir.addActionListener(e -> {

            double totalCOP = calcularTotalInterno();

            if (totalCOP <= 0) {
                JOptionPane.showMessageDialog(null, "No hay total para convertir");
                return;
            }

            String monedaDestino = comboMoneda.getSelectedItem().toString();

            double convertido = Api.ApiMoneda.convertir("COP", monedaDestino, totalCOP);

            if (convertido == 0) {
                JOptionPane.showMessageDialog(null, "Error al convertir moneda");
                return;
            }

            monedaActual = monedaDestino;

            lblTotal.setText("Total: "
                    + String.format("%.2f", convertido)
                    + " " + monedaDestino);
        });

        // ================= EVENTO AGREGAR PRODUCTO =================
        btnAgregar.addActionListener(e -> {

            if (comboProducto.getSelectedItem() == null || txtCantidad.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Seleccione producto y cantidad");
                return;
            }

            try {
                Producto producto = (Producto) comboProducto.getSelectedItem();
                int cantidad = Integer.parseInt(txtCantidad.getText());

                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "Cantidad inválida");
                    return;
                }

                double subtotal = producto.getPrecio() * cantidad;

                modeloTabla.addRow(new Object[]{
                        producto.getNombre(),
                        cantidad,
                        producto.getPrecio(),
                        subtotal
                });

                listaDetalle.add(new OrdenProducto(
                        0,
                        producto.getId(),
                        cantidad,
                        producto.getPrecio()
                ));

                calcularTotal();
                txtCantidad.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
            }
        });

        // ================= EVENTO ELIMINAR PRODUCTO =================
        btnEliminar.addActionListener(e -> {

            int filaSeleccionada = tabla.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla para eliminar");
                return;
            }

            listaDetalle.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);

            calcularTotal();
        });

        // ================= EVENTO CANCELAR ORDEN =================
        btnCancelar.addActionListener(e -> {
            limpiar();
            JOptionPane.showMessageDialog(null, "Orden cancelada");
        });

        // ================= EVENTO GUARDAR ORDEN =================
        btnGuardar.addActionListener(e -> {

            if (comboCliente.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente");
                return;
            }

            if (listaDetalle.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Agregue al menos un producto");
                return;
            }

            Cliente cliente = (Cliente) comboCliente.getSelectedItem();
            double totalCOP = calcularTotalInterno();

            Orden orden = new Orden(
                    new Date(),
                    totalCOP,
                    "preparacion",
                    cliente.getId(),
                    1,
                    1
            );

            int idGenerado = ordenDAO.insertar(orden);

            if (idGenerado == -1) {
                JOptionPane.showMessageDialog(null, "Error al guardar la orden");
                return;
            }

            for (OrdenProducto op : listaDetalle) {
                op.setIdOrden(idGenerado);
                ordenProductoDAO.insertar(op);
            }

            JOptionPane.showMessageDialog(null, "Orden guardada correctamente");
            limpiar();
        });
    }

    private void estilizarBoton(JButton boton, Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void cargarClientes() {
        for (Cliente c : clienteDAO.listar()) {
            comboCliente.addItem(c);
        }
    }

    private void cargarProductos() {
        for (Producto p : productoDAO.listar()) {
            comboProducto.addItem(p);
        }
    }

    private double calcularTotalInterno() {
        double total = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            total += Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
        }
        return total;
    }

    private void calcularTotal() {
        double total = calcularTotalInterno();
        monedaActual = "COP";
        lblTotal.setText("Total: $" + String.format("%.2f", total) + " COP");
    }

    private void limpiar() {
        modeloTabla.setRowCount(0);
        listaDetalle.clear();
        monedaActual = "COP";
        lblTotal.setText("Total: $0 COP");
    }
}