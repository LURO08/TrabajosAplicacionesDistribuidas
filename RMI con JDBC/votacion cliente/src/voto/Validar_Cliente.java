package voto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Validar_Cliente extends Remote{

public String[] verificar_votante(int dni)throws RemoteException;

public boolean verificar_voto(int dni)throws RemoteException;

public void enviar_voto(String voto,int dni)throws RemoteException;
    
    
}
