package com.mycompany.udpcliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPCliente {
    public static void main(String[] args) {
        try {
            // Crear el socket UDP del cliente
            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            int serverPort = 8080;

            // Hilo para recibir mensajes del servidor
            Thread receiverThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println("\nServidor: " + serverResponse);

                        // Si el servidor envía 'salir', terminar el cliente
                        if (serverResponse.trim().equalsIgnoreCase("salir")) {
                            System.out.println("Servidor ha terminado la conexión.");
                            clientSocket.close();
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error recibiendo datos: " + e.getMessage());
                }
            });

            // Iniciar el hilo para recibir mensajes
            receiverThread.start();

            // Loop principal para enviar mensajes al servidor
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Cliente envía mensaje al servidor
                System.out.print("Cliente: Escribe un mensaje para el servidor: ");
                String clientMessage = scanner.nextLine();
                byte[] sendData = clientMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);

                // Si el cliente envía 'salir', cerrar la conexión
                if (clientMessage.trim().equalsIgnoreCase("salir")) {
                    System.out.println("Cliente: Terminando la conexión...");
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en cliente: " + e.getMessage());
        }
    }
}
