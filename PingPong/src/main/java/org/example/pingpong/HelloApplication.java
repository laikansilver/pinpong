package org.example.pingpong;

import example.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("PingPongJugador1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("PingPong");
        stage.setScene(scene);
        stage.show();

        PingPongJugador1Controller controller = fxmlLoader.getController();
        controller.initialize("localhost", 6666); // replace with your server IP and port
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Server server = new Server(6666);
                server.start();
            } catch (IOException e) {
                System.out.println("Error al iniciar el servidor: " + e.getMessage());
            }
        }).start();

        launch();
    }
}