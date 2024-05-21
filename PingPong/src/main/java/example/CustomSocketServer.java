package example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CustomSocketServer {
    //atributos
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    //metodo para iniciar el servidor
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        //ciclo infinito para aceptar clientes
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clients.add(clientHandler);

            new Thread(clientHandler).start();
        }
    }

    //metodo para enviar mensaje a todos los clientes
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(sender.getName() + ": " + message);
            }
        }
    }

    //metodo para remover cliente
    public void stop() throws IOException {
        for (ClientHandler client : clients) {
            client.stop();
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        //iniciar el servidor en el puerto 6666
        CustomSocketServer server = new CustomSocketServer();
        server.start(6666);
    }
}