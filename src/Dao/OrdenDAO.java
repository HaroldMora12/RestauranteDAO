package Dao;

import conexion.ConexionDB;
import modelo.Orden;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO {

    private Connection con = ConexionDB.getConnection();

    // ================= INSERTAR =================
    public int insertar(Orden o) {

        String sql = "INSERT INTO `orden` (fecha, total, estado, id_cliente, id_empleado, id_mesa) VALUES (NOW(),?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, o.getTotal());
            ps.setString(2, o.getEstado());
            ps.setInt(3, o.getIdCliente());
            ps.setInt(4, o.getIdEmpleado());
            ps.setInt(5, o.getIdMesa());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // devuelve id_orden generado
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ================= LISTAR =================
    public List<Orden> listar() {

        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT * FROM `orden`";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Orden o = new Orden(
                        rs.getInt("id_orden"),
                        rs.getTimestamp("fecha"),
                        rs.getDouble("total"),
                        rs.getString("estado"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_empleado"),
                        rs.getInt("id_mesa")
                );

                lista.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ================= ACTUALIZAR ESTADO =================
    public boolean actualizarEstado(int idOrden, String nuevoEstado) {

        String sql = "UPDATE `orden` SET estado = ? WHERE id_orden = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idOrden);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= ELIMINAR =================
    public boolean eliminar(int idOrden) {

        String sql = "DELETE FROM `orden` WHERE id_orden = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
