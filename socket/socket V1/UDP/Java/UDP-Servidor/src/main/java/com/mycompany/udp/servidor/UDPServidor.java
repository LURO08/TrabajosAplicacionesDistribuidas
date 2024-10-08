/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.udp.servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPServidor {
    public static void main(String[] args) {
        try {
            int serverPort = 8080;
            // Crear el socket UDP en el puerto 12345
            DatagramSocket serverSocket = new DatagramSocket(serverPort);
            byte[] receiveData = new byte[1024];

            System.out.println("Servidor UDP escuchando en el puerto "+serverPort +"...");

            InetAddress clientAddress = null;
            int clientPort = 0;

            while (true) {
                // Recibir mensaje del cliente
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                clientAddress = receivePacket.getAddress();
                clientPort = receivePacket.getPort();
                
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Cliente: " + clientMessage);

                // Si el cliente envía 'salir', cerrar la conexión
                if (clientMessage.trim().equalsIgnoreCase("salir")) {
                    System.out.println("Servidor: Cliente terminó la conexión.");
                    break;
                }

                // Responder al cliente
                String response = "Servidor: Recibí tu mensaje '" + clientMessage + "'";
                byte[] responseData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                // Permitir que el servidor envíe mensajes también
                Scanner scanner = new Scanner(System.in);
                System.out.print("Servidor: Escribe un mensaje para el cliente: ");
                String serverMessage = scanner.nextLine();
                byte[] sendData = serverMessage.getBytes();
                DatagramPacket serverResponsePacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(serverResponsePacket);
            }

            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
