package com.mycompany.tcp.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServidor {

    public static void main(String[] args) {
        int port = 8080; // Puerto en el que escuchará el servidor

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            // Ciclo para aceptar conexiones de clientes
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Lanzar un hilo para manejar al cliente conectado
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    // Método para manejar la comunicación con el cliente
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Hilo para leer mensajes del cliente
            Thread readThread = new Thread(() -> {
                String clientMessage;
                try {
                    while ((clientMessage = in.readLine()) != null) {
                        System.out.println("Mensaje recibido del cliente: " + clientMessage);
                        // Responder con un eco al cliente
                        out.println("Echo: " + clientMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer del cliente: " + e.getMessage());
                }
            });

            // Hilo para enviar mensajes al cliente desde el servidor
            Thread writeThread = new Thread(() -> {
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                String serverMessage;
                try {
                    while ((serverMessage = consoleInput.readLine()) != null) {
                        out.println("Servidor: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Error al enviar mensaje al cliente: " + e.getMessage());
                }
            });

            // Iniciar los hilos para leer y escribir
            readThread.start();
            writeThread.start();

            // Esperar a que los hilos terminen antes de cerrar el socket
            readThread.join();
            writeThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Error en la conexión con el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Cliente desconectado");
            } catch (IOException e) {
                System.out.println("Error al cerrar la conexión del cliente: " + e.getMessage());
            }
        }
    }
}
