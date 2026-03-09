package vista;

import Dao.OrdenDAO;
import Dao.OrdenProductoDAO;
import modelo.Orden;
import modelo.OrdenProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenesGuardadasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private OrdenDAO ordenDAO = new OrdenDAO();
    private OrdenProductoDAO ordenProductoDAO = new OrdenProductoDAO();
    private JComboBox<String> comboEstado;

    public OrdenesGuardadasPanel() {

        setLayout(new BorderLayout(10,10));
        setBackground(Color.WHITE);

        // ===== PANEL SUPERIOR (FILTRO)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(Color.WHITE);

        comboEstado = new JComboBox<>(new String[]{
                "Todos",
                "preparacion",
                "lista",
                "entregada",
                "cancelada"
        });

        JButton btnFiltrar = new JButton("Filtrar");

        panelSuperior.add(new JLabel("Estado:"));
        panelSuperior.add(comboEstado);
        panelSuperior.add(btnFiltrar);

        add(panelSuperior, BorderLayout.NORTH);

        // ===== TABLA
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Total");
        modeloTabla.addColumn("Estado");

        tabla = new JTable(modeloTabla);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ===== PANEL INFERIOR
        JPanel panelInferior = new JPanel();

        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        JButton btnVerDetalle = new JButton("Ver Detalle");

        panelInferior.add(btnCambiarEstado);
        panelInferior.add(btnVerDetalle);

        add(panelInferior, BorderLayout.SOUTH);

        cargarOrdenes(null);

        // ===== EVENTOS

        btnFiltrar.addActionListener(e -> {
            String estado = comboEstado.getSelectedItem().toString();
            if (estado.equals("Todos")) {
                cargarOrdenes(null);
            } else {
                cargarOrdenes(estado);
            }
        });

        btnCambiarEstado.addActionListener(e -> cambiarEstado());

        btnVerDetalle.addActionListener(e -> verDetalle());
    }

    private void cargarOrdenes(String estadoFiltro) {

        modeloTabla.setRowCount(0);

        List<Orden> lista = ordenDAO.listar();

        for (Orden o : lista) {

            if (estadoFiltro == null || o.getEstado().equalsIgnoreCase(estadoFiltro)) {

                modeloTabla.addRow(new Object[]{
                        o.getId(),
                        o.getFecha(),
                        o.getTotal(),
                        o.getEstado()
                });
            }
        }
    }

    private void cambiarEstado() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una orden");
            return;
        }

        int idOrden = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        String[] opciones = {"preparacion", "lista", "entregada", "cancelada"};

        String nuevoEstado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione nuevo estado:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (nuevoEstado == null) return;

        boolean actualizado = ordenDAO.actualizarEstado(idOrden, nuevoEstado);

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Estado actualizado");
            cargarOrdenes(null);
        }
    }

    private void verDetalle() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una orden");
            return;
        }

        int idOrden = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        List<OrdenProducto> lista = ordenProductoDAO.listarPorOrden(idOrden);

        StringBuilder detalle = new StringBuilder();

        for (OrdenProducto op : lista) {

            detalle.append("Producto: ").append(op.getNombreProducto())
                    .append(" | Cantidad: ").append(op.getCantidad())
                    .append(" | Precio Unitario: $").append(op.getPrecioUnitario())
                    .append(" | Subtotal: $")
                    .append(op.getCantidad() * op.getPrecioUnitario())
                    .append("\n");

        }

        JOptionPane.showMessageDialog(null, detalle.toString(),
                "Detalle Orden " + idOrden,
                JOptionPane.INFORMATION_MESSAGE);
    }
}


