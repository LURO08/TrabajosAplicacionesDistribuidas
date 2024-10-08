package com.mycompany.udpcliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPCliente {
    private static DatagramSocket clientSocket;
    private static JTextArea textArea;
    private static JTextField ipField;
    private static JTextField portField;
    private static JTextField messageField;
    private static JButton connectButton;
    private static JButton disconnectButton;
    private static JButton sendButton;
    private static boolean isConnected = false;
    private static InetAddress serverAddress;
    private static int serverPort;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UDPCliente::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Cliente UDP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2, 5, 5));

        controlPanel.add(new JLabel("IP del Servidor:"));
        ipField = new JTextField("192.168.211.34");
        controlPanel.add(ipField);

        controlPanel.add(new JLabel("Puerto:"));
        portField = new JTextField("8080");
        controlPanel.add(portField);

        connectButton = new JButton("Conectar");
        disconnectButton = new JButton("Desconectar");
        disconnectButton.setEnabled(false);

        connectButton.addActionListener(new ConnectButtonListener());
        disconnectButton.addActionListener(new DisconnectButtonListener());

        controlPanel.add(connectButton);
        controlPanel.add(disconnectButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        messagePanel.add(new JLabel("Mensaje:"), BorderLayout.WEST);
        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Enviar");
        sendButton.addActionListener(new SendButtonListener());
        messagePanel.add(sendButton, BorderLayout.EAST);

        frame.add(messagePanel, BorderLayout.SOUTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isConnected) {
                try {
                    serverAddress = InetAddress.getByName(ipField.getText().trim());
                    serverPort = Integer.parseInt(portField.getText().trim());
                    clientSocket = new DatagramSocket();

                    isConnected = true;
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(true);
                    sendButton.setEnabled(true);

                    appendToTextArea("Conectado al servidor UDP en " + serverAddress.getHostAddress() + ":" + serverPort);

                    // Iniciar el hilo para recibir mensajes
                    new Thread(new ReceiverThread()).start();

                } catch (Exception ex) {
                    appendToTextArea("Error al conectar: " + ex.getMessage());
                }
            }
        }
    }

    private static class DisconnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isConnected) {
                isConnected = false;
                clientSocket.close();
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                sendButton.setEnabled(false);
                appendToTextArea("Desconectado del servidor");
            }
        }
    }

    private static class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isConnected) {
                String clientMessage = messageField.getText().trim();
                if (!clientMessage.isEmpty()) {
                    try {
                        byte[] sendData = clientMessage.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                        clientSocket.send(sendPacket);
                        appendToTextArea("Cliente: " + clientMessage);

                        // Si el cliente envía 'salir', cerrar la conexión
                        if (clientMessage.trim().equalsIgnoreCase("salir")) {
                            appendToTextArea("Cliente: Terminando la conexión...");
                            clientSocket.close();
                            isConnected = false;
                            connectButton.setEnabled(true);
                            disconnectButton.setEnabled(false);
                            sendButton.setEnabled(false);
                        }
                        messageField.setText("");
                    } catch (IOException ex) {
                        appendToTextArea("Error al enviar el mensaje: " + ex.getMessage());
                    }
                }
            }
        }
    }

    private static class ReceiverThread implements Runnable {
        @Override
        public void run() {
            try {
                byte[] receiveData = new byte[1024];
                while (isConnected) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    appendToTextArea("Servidor: " + serverResponse);

                    // Si el servidor envía 'salir', cerrar la conexión
                    if (serverResponse.trim().equalsIgnoreCase("salir")) {
                        appendToTextArea("Servidor ha terminado la conexión.");
                        clientSocket.close();
                        isConnected = false;
                        connectButton.setEnabled(true);
                        disconnectButton.setEnabled(false);
                        sendButton.setEnabled(false);
                        break;
                    }
                }
            } catch (IOException e) {
                if (isConnected) {
                    appendToTextArea("Error recibiendo datos: " + e.getMessage());
                }
            }
        }
    }

    private static void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
    }
}
