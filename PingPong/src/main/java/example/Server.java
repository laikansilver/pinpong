package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Scanner in;
    private PrintWriter out;

    public Server(int port) throws IOException {
        //iniciar el servidor en el puerto 6666
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        //ciclo infinito para aceptar clientes
        System.out.println("esperando coneccion del cliente...");
        clientSocket = serverSocket.accept();
        System.out.println("coneccion cliente!");
        in = new Scanner(clientSocket.getInputStream());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void send(String message) {
        out.println(message);
    }

    public String receive() {
        //recibir mensaje del cliente
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return null;
    }

    public void stop() throws IOException {
        //cerrar el servidor
        clientSocket.close();
        serverSocket.close();
    }
}