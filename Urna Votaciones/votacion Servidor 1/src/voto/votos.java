package voto;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface votos extends Remote{
    
 public String[][] recuento()throws RemoteException;
 
 public void votar(String candidato)throws RemoteException;
    
}
