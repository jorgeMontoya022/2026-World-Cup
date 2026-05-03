package co.edu.uniquindio.cup.world_app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton que gestiona la conexión JDBC a MySQL.
 * Configurar URL, usuario y contraseña según el entorno local.
 */
public class ConexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3307/mundial2026?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1097032932";

    private static ConexionDB instancia;
    private Connection conexion;

    private ConexionDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna la instancia única. Si la conexión está cerrada la recrea.
     */
    public static ConexionDB getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        }
        return conexion;
    }

    /** Cierra la conexión de forma segura. */
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
