package Dao;

import conexion.ConexionDB;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    Connection con = ConexionDB.getConnection();

    // ================= LISTAR =================
    public List<Producto> listar() {

        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("disponibilidad")
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ================= AGREGAR =================
    public void agregar(Producto p) {

        String sql = "INSERT INTO productos(nombre, categoria, precio, disponibilidad) VALUES (?,?,?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getDisponibilidad());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= ACTUALIZAR =================
    public void actualizar(Producto p) {

        String sql = "UPDATE productos SET nombre=?, categoria=?, precio=?, disponibilidad=? WHERE id_producto=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getDisponibilidad());
            ps.setInt(5, p.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= ELIMINAR =================
    public void eliminar(int id) {

        String sql = "DELETE FROM productos WHERE id_producto=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
