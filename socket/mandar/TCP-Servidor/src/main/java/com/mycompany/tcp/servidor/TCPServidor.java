package com.mycompany.tcp.servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServidor {
    private static ServerSocket serverSocket;
    private static JTextArea textArea;
    private static JButton startButton;
    private static JButton stopButton;
    private static JTextField messageField;
    private static JTextField ipField;
    private static JTextField portField;
    private static boolean isRunning = false;
    private static PrintWriter clientOut; // Para enviar mensajes a los clientes

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TCPServidor::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Servidor TCP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 5, 5));
        
        topPanel.add(new JLabel("IP:"));
        ipField = new JTextField("localhost");
        topPanel.add(ipField);

        topPanel.add(new JLabel("Puerto:"));
        portField = new JTextField("8080");
        topPanel.add(portField);

        startButton = new JButton("Iniciar Servidor");
        stopButton = new JButton("Detener Servidor");
        stopButton.setEnabled(false);

        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());

        topPanel.add(startButton);
        topPanel.add(stopButton);

        controlPanel.add(topPanel, BorderLayout.NORTH);

        controlPanel.add(new JLabel("Mensaje:"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new SendButtonListener());
        bottomPanel.add(sendButton, BorderLayout.EAST);

        controlPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(controlPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isRunning) {
                String ip = ipField.getText().trim();
                int port;
                try {
                    port = Integer.parseInt(portField.getText().trim());
                } catch (NumberFormatException ex) {
                    appendToTextArea("Número de puerto inválido");
                    return;
                }
                
                isRunning = true;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                startServer(ip, port);
            }
        }
    }

    private static class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRunning) {
                isRunning = false;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                stopServer();
            }
        }
    }

    private static class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clientOut != null) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    clientOut.println(message);
                    appendToTextArea("Enviado: " + message);
                    messageField.setText("");
                }
            } else {
                appendToTextArea("No hay clientes conectados.");
            }
        }
    }

    private static void startServer(String ip, int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port, 50, java.net.InetAddress.getByName(ip));
                appendToTextArea("Servidor escuchando en " + ip + ":" + port);

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                appendToTextArea("Error al iniciar el servidor: " + e.getMessage());
            }
        }).start();
    }

    private static void stopServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                appendToTextArea("Servidor detenido");
            }
        } catch (IOException e) {
            appendToTextArea("Error al detener el servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                clientOut = new PrintWriter(clientSocket.getOutputStream(), true); // Inicializar para enviar mensajes
                String message;
                while ((message = in.readLine()) != null) {
                    appendToTextArea("Recibido: " + message);
                }
            } catch (IOException e) {
                appendToTextArea("Error con el cliente: " + e.getMessage());
            }
        }).start();
    }

    private static void appendToTextArea(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text + "\n");
        });
    }
}
