package com.mycompany.tcp.cliente;

import java.io.*;
import java.net.Socket;

public class TCPCliente {

    public static void main(String[] args) {
        String host = "localhost"; // Dirección del servidor
        int port = 8080; // Puerto al que se conecta el cliente

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor en " + host + ":" + port);

            // Hilo para recibir mensajes del servidor
            Thread readThread = new Thread(() -> {
                String serverResponse;
                try {
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Servidor: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer la respuesta del servidor: " + e.getMessage());
                }
            });

            // Hilo para enviar mensajes al servidor
            Thread writeThread = new Thread(() -> {
                String userInput;
                try {
                    while ((userInput = stdIn.readLine()) != null) {
                        out.println(userInput); // Enviar mensaje al servidor
                        System.out.println("Cliente: " + userInput); // Mostrar el mensaje enviado en consola
                    }
                } catch (IOException e) {
                    System.out.println("Error al enviar el mensaje al servidor: " + e.getMessage());
                }
            });

            // Iniciar ambos hilos
            readThread.start();
            writeThread.start();

            // Esperar a que los hilos terminen
            readThread.join();
            writeThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Error en la conexión con el servidor: " + e.getMessage());
        }
    }
}
