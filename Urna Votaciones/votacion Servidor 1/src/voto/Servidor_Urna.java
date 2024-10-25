/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voto;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor_Urna {
    
            public static void main(String[] args) {
        try{
            
            Registry register=LocateRegistry.createRegistry(4070);
            register.rebind("urna", new Implementar_Voto());
            
            System.out.println("servidor urna en linea");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
