package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/restaurante?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Conectado a MySQL");
            return con;
        } catch (SQLException e) {
            System.out.println("❌ NO se pudo conectar a MySQL");
            e.printStackTrace();
            return null;
        }
    }
}
