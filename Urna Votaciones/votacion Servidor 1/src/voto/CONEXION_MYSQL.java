package voto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CONEXION_MYSQL {

    private static final String URL = "jdbc:mysql://localhost/urna";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conexion;

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para conectar a la base de datos
    public Connection conectar() throws SQLException, ClassNotFoundException {
        if (conexion == null || conexion.isClosed()) {
            // Cargar el controlador de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }

    // Método para ejecutar una consulta (SELECT)
    public ResultSet consultar(String sql) throws SQLException {
        ResultSet resultado = null;
        Statement sentencia = null;
        try {
            sentencia = getConexion().createStatement();
            resultado = sentencia.executeQuery(sql);
        } finally {
            // No cerramos el ResultSet aquí porque debe ser manejado externamente
            if (sentencia != null) {
                sentencia.close();
            }
        }
        return resultado;
    }

    // Método para ejecutar una actualización (INSERT, UPDATE, DELETE)
    public boolean ejecutar(String sql) throws SQLException {
        Statement sentencia = null;
        try {
            sentencia = getConexion().createStatement();
            sentencia.executeUpdate(sql);
            return true;
        } finally {
            if (sentencia != null) {
                sentencia.close();
            }
        }
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
