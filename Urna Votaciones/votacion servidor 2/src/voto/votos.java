package voto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface votos extends Remote{
    
 public String[][] recuento()throws RemoteException;
 
 public void votar(String candidato)throws RemoteException;
 
 public boolean registrarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException ;

public  boolean actualizarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException;

public boolean eliminarElector(String dni) throws RemoteException;
}
