package voto;

import java.sql.*;
import javax.swing.JOptionPane;

public class CONEXION_MYSQL {

    private Connection conexion;

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public CONEXION_MYSQL conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String baseDeDatos = "jdbc:mysql://localhost/votantes?user=root&password=";
            setConexion(DriverManager.getConnection(baseDeDatos));
            System.out.println("Conexión establecida exitosamente.");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver de MySQL no encontrado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return this;
    }

    public ResultSet consultar(String sql) {
        ResultSet resultado = null;
        Statement sentencia = null;
        try {
            sentencia = getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerramos la sentencia si ocurrió un error
            if (resultado == null && sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la sentencia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return resultado;
    }

    public boolean ejecutar(String sql) {
        Statement sentencia = null;
        try {
            sentencia = getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            sentencia.executeUpdate(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // Asegurarse de cerrar la sentencia
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la sentencia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return true;
    }

    public void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
