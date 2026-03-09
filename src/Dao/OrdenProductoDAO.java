package Dao;

import conexion.ConexionDB;
import modelo.OrdenProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenProductoDAO {

    private Connection con = ConexionDB.getConnection();

    // ================= INSERTAR =================
    public void insertar(OrdenProducto op) {

        String sql = "INSERT INTO orden_producto(id_orden, id_producto, cantidad, precio_unitario) VALUES (?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, op.getIdOrden());
            ps.setInt(2, op.getIdProducto());
            ps.setInt(3, op.getCantidad());
            ps.setDouble(4, op.getPrecioUnitario());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= LISTAR POR ORDEN =================
    public List<OrdenProducto> listarPorOrden(int idOrden) {

        List<OrdenProducto> lista = new ArrayList<>();

        String sql = """
            SELECT op.id_producto, p.nombre, op.cantidad, op.precio_unitario
            FROM orden_producto op
            JOIN productos p ON op.id_producto = p.id_producto
            WHERE op.id_orden = ?
            """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrdenProducto op = new OrdenProducto(
                        idOrden,
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                );

                // 👇 nuevo campo
                op.setNombreProducto(rs.getString("nombre"));

                lista.add(op);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ================= ELIMINAR POR ORDEN =================
    public boolean eliminarPorOrden(int idOrden) {

        String sql = "DELETE FROM orden_producto WHERE id_orden = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
