package org.example.pingpong;

import example.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PingPongJugador1Controller {
    @FXML
    private Rectangle paddle1;
    @FXML
    private Rectangle paddle2;
    @FXML
    private Circle ball;
    @FXML
    private Label scoreLabel1;
    @FXML
    private Label scoreLabel2;

    private int ballDirX = 1;
    private int ballDirY = 1;
    private int score1 = 0;
    private int score2 = 0;
    private Client client;

    public void initialize(String ip, int port) throws IOException {
        client = new Client(ip, port);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> updateGame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = paddle1.getScene();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                paddle1.setLayoutY(paddle1.getLayoutY() - 30);
            } else if (event.getCode() == KeyCode.S) {
                paddle1.setLayoutY(paddle1.getLayoutY() + 30);
            }
        });

        Stage stage = (Stage) scene.getWindow();
        stage.setTitle("PingPong");
        stage.setScene(scene);
    }

    private void updateGame() {
        // Update ball position
        ball.setLayoutX(ball.getLayoutX() + ballDirX);
        ball.setLayoutY(ball.getLayoutY() + ballDirY);

        // Check for collision with paddles
        if (ball.getBoundsInParent().intersects(paddle1.getBoundsInParent())) {
            ballDirX *= -1;
            score1++;
            scoreLabel1.setText("Jugador 1: " + score1);
        } else if (ball.getBoundsInParent().intersects(paddle2.getBoundsInParent())) {
            ballDirX *= -1;
            score2++;
            scoreLabel2.setText("Jugador 2: " + score2);
        }

        // Check for collision with top or bottom of game pane
        if (ball.getLayoutY() <= 0 || ball.getLayoutY() >= paddle1.getParent().getBoundsInLocal().getHeight() - ball.getRadius() * 2) {
            ballDirY *= -1;
        }

        // Send game state to player 2
        client.send(String.format("%d %d %d %d %d %d", (int) paddle1.getLayoutY(), (int) paddle2.getLayoutY(), (int) ball.getLayoutX(), (int) ball.getLayoutY(), ballDirX, ballDirY));
    }
}