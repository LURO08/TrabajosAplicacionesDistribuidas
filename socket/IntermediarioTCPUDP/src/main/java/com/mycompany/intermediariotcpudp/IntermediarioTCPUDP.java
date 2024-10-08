package com.mycompany.intermediariotcpudp;
import java.io.*;
import java.net.*;

public class IntermediarioTCPUDP{
    private static ServerSocket tcpServerSocket;
    private static DatagramSocket udpSocket;
    private static Socket tcpClientSocket;
    private static PrintWriter tcpOut;
    private static BufferedReader tcpIn;

    public static void main(String[] args) {
        try {
            // Inicializa el servidor TCP en un puerto especificado
            tcpServerSocket = new ServerSocket(8081); // Puedes ajustar el puerto
            System.out.println("Esperando conexión TCP...");

            // Conectar con el cliente TCP
            tcpClientSocket = tcpServerSocket.accept();
            System.out.println("Cliente TCP conectado.");
            tcpOut = new PrintWriter(tcpClientSocket.getOutputStream(), true);
            tcpIn = new BufferedReader(new InputStreamReader(tcpClientSocket.getInputStream()));

            // Inicializa el socket UDP
            udpSocket = new DatagramSocket(8080); // Puerto UDP para recibir mensajes
            System.out.println("Esperando mensajes UDP...");

            // Correr el ciclo de comunicación
            while (true) {
                // Recibir mensajes del cliente UDP
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);
                String udpMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Mensaje recibido desde el cliente UDP: " + udpMessage);

                // Reenviar el mensaje al servidor TCP
                tcpOut.println(udpMessage);
                System.out.println("Mensaje enviado al servidor TCP.");

                // Recibir respuesta del servidor TCP
                String tcpResponse = tcpIn.readLine();
                System.out.println("Respuesta recibida del servidor TCP: " + tcpResponse);

                // Enviar la respuesta de vuelta al cliente UDP
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] sendData = tcpResponse.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                udpSocket.send(sendPacket);
                System.out.println("Respuesta enviada al cliente UDP.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (tcpClientSocket != null) tcpClientSocket.close();
                if (udpSocket != null) udpSocket.close();
                if (tcpServerSocket != null) tcpServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
