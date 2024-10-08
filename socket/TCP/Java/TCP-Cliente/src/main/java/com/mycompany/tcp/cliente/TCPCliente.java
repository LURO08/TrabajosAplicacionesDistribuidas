package com.mycompany.tcp.cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class TCPCliente {
    private static Socket socket;
    private static JTextArea textArea;
    private static JButton connectButton;
    private static JButton disconnectButton;
    private static JTextField ipField;
    private static JTextField portField;
    private static JTextField messageField;
    private static BufferedReader in;
    private static PrintWriter out;
    private static boolean isConnected = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TCPCliente::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Cliente TCP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel de conexión
        JPanel connectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ipField = new JTextField("localhost");
        portField = new JTextField("8080");

        connectionPanel.add(new JLabel("IP:"));
        connectionPanel.add(ipField);
        connectionPanel.add(new JLabel("Puerto:"));
        connectionPanel.add(portField);

        connectButton = new JButton("Conectar");
        disconnectButton = new JButton("Desconectar");
        disconnectButton.setEnabled(false);

        connectButton.addActionListener(new ConnectButtonListener());
        disconnectButton.addActionListener(new DisconnectButtonListener());

        connectionPanel.add(connectButton);
        connectionPanel.add(disconnectButton);

        frame.add(connectionPanel, BorderLayout.NORTH);

        // Panel de mensaje
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new SendButtonListener());

        messagePanel.add(new JLabel("Mensaje:"), BorderLayout.WEST);
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        frame.add(messagePanel, BorderLayout.SOUTH);

        // Área de texto para mostrar mensajes
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(230, 230, 230));
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isConnected) {
                String ip = ipField.getText().trim();
                int port;
                try {
                    port = Integer.parseInt(portField.getText().trim());
                } catch (NumberFormatException ex) {
                    appendToTextArea("Número de puerto inválido");
                    return;
                }

                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                connectToServer(ip, port);
            }
        }
    }

    private static class DisconnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isConnected) {
                disconnectButton.setEnabled(false);
                connectButton.setEnabled(true);
                disconnectFromServer();
            }
        }
    }

    private static class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isConnected) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    out.println(message);
                    appendToTextArea("Cliente: " + message);
                    messageField.setText("");
                }
            } else {
                appendToTextArea("No estás conectado al servidor");
            }
        }
    }

    private static void connectToServer(String ip, int port) {
        new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                appendToTextArea("Conectado al servidor en " + ip + ":" + port);
                isConnected = true;

                Thread readThread = new Thread(() -> {
                    String serverResponse;
                    try {
                        while ((serverResponse = in.readLine()) != null) {
                            appendToTextArea("Servidor: " + serverResponse);
                        }
                    } catch (IOException e) {
                        appendToTextArea("Error al leer la respuesta del servidor: " + e.getMessage());
                    }
                });
                readThread.start();
            } catch (IOException e) {
                appendToTextArea("Error en la conexión con el servidor: " + e.getMessage());
                disconnectFromServer(); 
            }
        }).start();
    }

    private static void disconnectFromServer() {
        try {
            if (socket != null) {
                socket.close();
                appendToTextArea("Desconectado del servidor");
                isConnected = false;
            }
        } catch (IOException e) {
            appendToTextArea("Error al desconectar del servidor: " + e.getMessage());
        }
    }

    private static void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> textArea.append(text + "\n"));
    }
}
