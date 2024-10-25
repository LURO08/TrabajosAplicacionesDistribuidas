/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package voto;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor_Validacion {
    
    public static void main(String[] args) {
        try{
            
            Registry register=LocateRegistry.createRegistry(1099);
            register.rebind("validar_cliente", new Implementar_Validar_Cliente());
            
            System.out.println("servidor de validacion en linea");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
