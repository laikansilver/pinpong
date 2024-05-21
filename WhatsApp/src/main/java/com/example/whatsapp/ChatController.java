package com.example.whatsapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.Socket;

public class ChatController {

    @FXML
    private VBox messagesContainer;

    @FXML
    private TextField messageField;

    private String username;
    private String serverIp;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void initialize(String name, String ip, int port) {
        this.username = name;
        this.serverIp = ip;
        this.serverPort = port;
        try {
            this.socket = new Socket(serverIp, serverPort);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Enviar el nombre del usuario al servidor
            writer.write(username);
            writer.newLine();
            writer.flush();

            // Crear y lanzar un hilo para recibir mensajes
            Thread receiver = new Thread(new MessageReceiver(this));
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
                addMessageToChat(username + ": " + message);
                messageField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessageToChat(String message) {
        Platform.runLater(() -> {
            Text text = new Text(message);
            messagesContainer.getChildren().add(new TextFlow(text));
        });
    }

    private static class MessageReceiver implements Runnable {
        private ChatController chatController;

        public MessageReceiver(ChatController chatController) {
            this.chatController = chatController;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = chatController.reader.readLine()) != null) {
                    chatController.addMessageToChat(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}