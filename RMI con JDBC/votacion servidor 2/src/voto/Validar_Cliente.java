package voto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Validar_Cliente extends Remote{

public String[] verificar_votante(int dni)throws RemoteException;

public boolean verificar_voto(int dni)throws RemoteException;

public void enviar_voto(String voto,int dni)throws RemoteException;

public boolean registrarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException;
    
List<String[]> obtenerTodosElectores() throws RemoteException; // Nueva línea

public boolean actualizarElector(String dni, String nombre, String apellidoPaterno, String apellidoMaterno, String direccion) throws RemoteException;
    
public boolean eliminarElector(String dni) throws RemoteException;
}
