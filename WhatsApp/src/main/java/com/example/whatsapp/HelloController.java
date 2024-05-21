package com.example.whatsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private void connect() {
        String name = nameField.getText();
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
            Parent root = loader.load();

            ChatController chatController = loader.getController();
            chatController.initialize(name, ip, port);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("laikansilver Chat - " + name);
            stage.show();

            Stage currentStage = (Stage) nameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}