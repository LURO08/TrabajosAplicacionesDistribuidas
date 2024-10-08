package com.mycompany.udp.servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServidor {
    private static DatagramSocket serverSocket;
    private static JTextArea textArea;
    private static JTextField portField;
    private static JTextField messageField;
    private static JButton startButton;
    private static JButton stopButton;
    private static boolean isRunning = false;
    private static InetAddress clientAddress;
    private static int clientPort;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UDPServidor::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Servidor UDP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 2, 5, 5));

        controlPanel.add(new JLabel("Puerto:"));
        portField = new JTextField("8080");
        controlPanel.add(portField);

        startButton = new JButton("Iniciar Servidor");
        stopButton = new JButton("Detener Servidor");
        stopButton.setEnabled(false);

        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        messagePanel.add(new JLabel("Mensaje:"), BorderLayout.WEST);
        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Enviar"); // El botón de envío solo se habilitará cuando el servidor esté corriendo
        sendButton.addActionListener(new SendButtonListener());
        messagePanel.add(sendButton, BorderLayout.EAST);

        frame.add(messagePanel, BorderLayout.SOUTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isRunning) {
                int port;
                try {
                    port = Integer.parseInt(portField.getText().trim());
                } catch (NumberFormatException ex) {
                    appendToTextArea("Número de puerto inválido");
                    return;
                }

                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                isRunning = true; // Indicar que el servidor está corriendo
                startServer(port);
            }
        }
    }

    private static class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRunning) {
                isRunning = false;
                stopServer();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        }
    }

    private static class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clientAddress != null && clientPort > 0) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    try {
                        byte[] sendData = message.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                        serverSocket.send(sendPacket);
                        appendToTextArea("Servidor: " + message);
                        messageField.setText("");
                    } catch (IOException ex) {
                        appendToTextArea("Error al enviar el mensaje: " + ex.getMessage());
                    }
                }
            } else {
                appendToTextArea("No hay cliente conectado");
            }
        }
    }

    private static void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new DatagramSocket(port);
                appendToTextArea("Servidor UDP escuchando en el puerto " + port);

                byte[] receiveData = new byte[1024];
                while (isRunning) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    clientAddress = receivePacket.getAddress();
                    clientPort = receivePacket.getPort();

                    String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    appendToTextArea("Cliente: " + clientMessage);

                    // Responder al cliente
                    String response = "Servidor: Recibí tu mensaje '" + clientMessage + "'";
                    byte[] responseData = response.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                }
            } catch (IOException e) {
                appendToTextArea("Error al iniciar el servidor: " + e.getMessage());
            }
        }).start();
    }

    private static void stopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            appendToTextArea("Servidor detenido");
        }
    }

    private static void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
    }
}
