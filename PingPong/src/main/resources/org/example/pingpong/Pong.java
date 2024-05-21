

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Pong extends Application {

    //variable
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private int playerOneXPos = 0;
    private double playerTwoXPos = width - PLAYER_WIDTH;
    

    //inicio de la aplicacion
    public void start(Stage stage) throws Exception {
        stage.setTitle("P O N G");
        //background size
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //JavaFX Timeline = free form animation defined by KeyFrames and their duration
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        //number of cycles in animation INDEFINITE = repeat indefinitely
        tl.setCycleCount(Timeline.INDEFINITE);


        // Control del teclado
        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    playerOneYPos -= 20;
                    break;
                case S:
                    playerOneYPos += 20;
                    break;
                case UP:
                    playerTwoYPos -= 20;
                    break;
                case DOWN:
                    playerTwoYPos += 20;
                    break;
                case SPACE:
                case ENTER:
                    gameStarted = true;
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
        tl.play();

        /*//mouse control (move and click)
        canvas.setOnMouseMoved(e ->  playerOneYPos  = e.getY());
        canvas.setOnMouseClicked(e ->  gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();*/
    }

    //metodo para correr el juego
    private void run(GraphicsContext gc) {
        // Establecer gráficos
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Establecer texto
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));

        if(gameStarted) {
            // Establecer movimiento de la pelota
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            // Dibujar la pelota
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
        } else {
            // Establecer el texto de inicio
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click", width / 2, height / 2);

            // Restablecer la posición inicial de la pelota
            ballXPos = width / 2;
            ballYPos = height / 2;

            // Restablecer la velocidad y la dirección de la pelota
            ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
        }

        // Asegurarse de que la pelota permanezca en el lienzo
        if(ballYPos > height || ballYPos < 0) ballYSpeed *= -1;

        // Si te pierdes la pelota, el ordenador obtiene un punto
        if(ballXPos < playerOneXPos - PLAYER_WIDTH) {
            scoreP2++;
            gameStarted = false;
        }

        // Si el ordenador se pierde la pelota, obtienes un punto
        if(ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            scoreP1++;
            gameStarted = false;
        }

        // Aumentar la velocidad después de que la pelota golpee al jugador
        if( ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
                ((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        // Dibujar puntuación
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
        // Dibujar jugador 1 y 2
        gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    // start the application
    public static void main(String[] args) {
        launch(args);
    }
}