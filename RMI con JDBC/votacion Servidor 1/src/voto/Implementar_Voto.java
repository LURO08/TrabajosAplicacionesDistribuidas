package voto;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Implementar_Voto extends UnicastRemoteObject implements votos {

    public Implementar_Voto() throws RemoteException {
    }

    @Override
    public String[][] recuento() throws RemoteException {
        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultado = null;
        List<String[]> registros = new ArrayList<>();
        String[][] registroFinal = null;

        String sql = "SELECT candidato AS CANDIDATO, COUNT(candidato) AS VOTOS FROM urna GROUP BY candidato";

        try {
            conexion = mysql.conectar();
            preparedStatement = conexion.prepareStatement(sql);
            resultado = preparedStatement.executeQuery();

            while (resultado.next()) {
                String candidato = resultado.getString("CANDIDATO");
                String votos = Integer.toString(resultado.getInt("VOTOS"));
                registros.add(new String[]{candidato, votos});
            }

            // Convertir la lista en un arreglo bidimensional
            registroFinal = new String[registros.size()][3];
            for (int i = 0; i < registros.size(); i++) {
                registroFinal[i] = registros.get(i);
            }

        } catch (SQLException e) {
            Logger.getLogger(Implementar_Voto.class.getName()).log(Level.SEVERE, "Error al realizar el recuento de votos", e);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Implementar_Voto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar los recursos
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Implementar_Voto.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }

        return registroFinal;
    }

    @Override
    public void votar(String candidato) throws RemoteException {
        System.out.println("Voto recibido en Servidor Urna: " + candidato);

        CONEXION_MYSQL mysql = new CONEXION_MYSQL();
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        try {
            conexion = mysql.conectar();

            // Usamos una consulta preparada para evitar inyecciones SQL
            String query = "INSERT INTO urna (candidato) VALUES (?)";
            preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, candidato);

            // Ejecutamos la consulta
            preparedStatement.executeUpdate();

            System.out.println("Voto registrado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al registrar el voto: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Implementar_Voto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerramos los recursos en el bloque finally para garantizar que se liberen
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}
