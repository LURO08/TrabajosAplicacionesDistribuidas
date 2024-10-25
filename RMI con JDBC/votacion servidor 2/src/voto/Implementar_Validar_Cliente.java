package voto;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Implementar_Validar_Cliente extends UnicastRemoteObject implements Validar_Cliente {

    public Implementar_Validar_Cliente() throws RemoteException {
    }

    @Override
    public String[] verificar_votante(int dni) throws RemoteException {

        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        mysql.conectar();

        String sql = "select nombre, apellido_paterno, apellido_materno, direccion from electores where dni = " + dni + ";";

        ResultSet resultado = mysql.consultar(sql);

        String datos[] = new String[4];
        if (resultado != null) {
            try {
                while (resultado.next()) {

                    datos[0] = resultado.getString("nombre");
                    datos[1] = resultado.getString("apellido_paterno");
                    datos[2] = resultado.getString("apellido_materno");
                    datos[3] = resultado.getString("direccion");

                }
            } catch (SQLException ex) {
                Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return datos;
    }

    @Override
    public boolean verificar_voto(int dni) throws RemoteException {
        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        mysql.conectar();
        Connection connection = mysql.getConexion(); // Obtiene la conexión

        String sql = "SELECT voto FROM electores WHERE dni = ?";
        boolean voto = false;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dni);

            ResultSet resultado = statement.executeQuery();
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "resultado" + resultado);

            if (resultado != null && resultado.next()) {
                voto = resultado.getBoolean("voto");
            }
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Consulta realizad:" + voto);

        } catch (SQLException ex) {
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error en la base de datos", ex);
        } finally {
            mysql.desconectar(); // Asegurarse de cerrar la conexión
        }

        return voto;
    }

    @Override

    public void enviar_voto(String voto, int dni) throws RemoteException {
        Registry register = null;
        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        Connection connection = null;
        System.out.println("voto recibido a servidor");
        System.out.println("Voto por: " + voto);
        System.out.println("DNI: " + dni);

        try {
            // Conectar con el registro RMI
            register = LocateRegistry.getRegistry("localhost", 4070);
            votos miInterfaz = (votos) register.lookup("urna");

            System.out.println("Enviando Voto");
            // Enviar voto a través de RMI
            miInterfaz.votar(voto);
            System.out.println("Voto despues de envio: " + voto);

            // Conectar a la base de datos
            mysql.conectar();
            connection = mysql.getConexion(); // Obtiene la conexión

            // Usar PreparedStatement para evitar SQL Injection
            String sql = "UPDATE electores SET voto = true WHERE dni = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, dni);
                statement.executeUpdate();
            }

        } catch (RemoteException | NotBoundException e) {
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error en la conexión RMI", e);
        } catch (SQLException e) {
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error en la base de datos", e);
        } finally {
            // Asegurarse de cerrar la conexión a la base de datos
            if (connection != null) {
                try {
                    mysql.desconectar();
                } catch (Exception e) {
                    Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", e);
                }
            }
        }
    }

    public boolean registrarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException {
        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        Connection connection = null;
        boolean registrado = false;

        try {
            // Conectar a la base de datos
            mysql.conectar();
            connection = mysql.getConexion(); // Obtiene la conexión

            // Usar PreparedStatement para evitar SQL Injection
            String sql = "INSERT INTO electores (dni, nombre, apellido_paterno, apellido_materno, direccion, voto) VALUES (?, ?, ?, ?, ?, false)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, dni);
                statement.setString(2, nombre);
                statement.setString(3, apellidoPaterno);
                statement.setString(4, apellidoMaterno);
                statement.setString(5, direccion);

                // Ejecutar la inserción
                int filasAfectadas = statement.executeUpdate();
                registrado = filasAfectadas > 0; // Si se afectó al menos una fila, el registro fue exitoso
            }

       } catch (SQLException e) {
            Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error en la base de datos", e);
        } finally {
            // Asegurarse de cerrar la conexión a la base de datos
            if (connection != null) {
                try {
                    mysql.desconectar();
                } catch (Exception e) {
                    Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", e);
                }
            }
        }

        return registrado;
    }

@Override
public List<String[]> obtenerTodosElectores() throws RemoteException {
    List<String[]> listaElectores = new ArrayList<>();
    CONEXION_MYSQL mysql = new CONEXION_MYSQL();
    mysql.conectar();

    String sql = "SELECT dni, nombre, apellido_paterno, apellido_materno, direccion FROM electores;";
    
    try {
        ResultSet resultado = mysql.consultar(sql);
        while (resultado.next()) {
            String[] datos = new String[5];
            datos[0] = String.valueOf(resultado.getInt("dni"));
            datos[1] = resultado.getString("nombre");
            datos[2] = resultado.getString("apellido_paterno");
            datos[3] = resultado.getString("apellido_materno");
            datos[4] = resultado.getString("direccion");
            listaElectores.add(datos);
        }
    } catch (SQLException ex) {
        Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        mysql.desconectar(); // Asegurarse de cerrar la conexión
    }

    return listaElectores;
}

    @Override
    public boolean actualizarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException {
    CONEXION_MYSQL mysql = new CONEXION_MYSQL();
    Connection connection = null;
    boolean actualizado = false;

    try {
        // Conectar a la base de datos
        mysql.conectar();
        connection = mysql.getConexion(); // Obtiene la conexión

        // Usar PreparedStatement para evitar SQL Injection
        String sql = "UPDATE electores SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, direccion = ? WHERE dni = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, apellidoPaterno);
            statement.setString(3, apellidoMaterno);
            statement.setString(4, direccion);
            statement.setString(5, dni); // Establece el DNI como condición para la actualización

            // Ejecutar la actualización
            int filasAfectadas = statement.executeUpdate();
            actualizado = filasAfectadas > 0; // Si se afectó al menos una fila, la actualización fue exitosa
        }

    } catch (SQLException e) {
        Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al actualizar elector en la base de datos", e);
    } finally {
        // Asegurarse de cerrar la conexión a la base de datos
        if (connection != null) {
            try {
                mysql.desconectar();
            } catch (Exception e) {
                Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }

    return actualizado;
}
    
    public boolean eliminarElector(String dni) throws RemoteException {
    CONEXION_MYSQL mysql = new CONEXION_MYSQL();
    Connection connection = null;
    boolean eliminado = false;

    try {
        // Conectar a la base de datos
        mysql.conectar();
        connection = mysql.getConexion(); // Obtiene la conexión

        // Usar PreparedStatement para evitar SQL Injection
        String sql = "DELETE FROM electores WHERE dni = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dni);

            // Ejecutar la eliminación
            int filasAfectadas = statement.executeUpdate();
            eliminado = filasAfectadas > 0; // Si se afectó al menos una fila, la eliminación fue exitosa
        }

    } catch (SQLException e) {
        Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al eliminar elector de la base de datos", e);
    } finally {
        // Asegurarse de cerrar la conexión a la base de datos
        if (connection != null) {
            try {
                mysql.desconectar();
            } catch (Exception e) {
                Logger.getLogger(Implementar_Validar_Cliente.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }

    return eliminado;
}





    



}
