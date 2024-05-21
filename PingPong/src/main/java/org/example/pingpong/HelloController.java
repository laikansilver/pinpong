package org.example.pingpong;

import example.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private CheckBox player1CheckBox;

    @FXML
    private CheckBox player2CheckBox;

    private Client client;

   @FXML
    private void connect() {
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        boolean isPlayer1 = player1CheckBox.isSelected();
        boolean isPlayer2 = player2CheckBox.isSelected();

        try {
            FXMLLoader fxmlLoader;
            if (isPlayer1) {
                fxmlLoader = new FXMLLoader(getClass().getResource("PingPongJugador1.fxml"));
            } else if (isPlayer2) {
                fxmlLoader = new FXMLLoader(getClass().getResource("PongPingJugador2.fxml"));
            } else {
                System.out.println("Error: No se ha seleccionado un jugador");
                return;
            }

            //cargo el fxml
            Parent root = fxmlLoader.load();

            // asigno los valores a los campos del controlador
            Object controller = fxmlLoader.getController();
            if (controller instanceof PingPongJugador1Controller) {
                ((PingPongJugador1Controller) controller).initialize(ip, port);
            } else if (controller instanceof PongPingJugador2Controller) {
                ((PongPingJugador2Controller) controller).initialize(ip, port);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ipField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}