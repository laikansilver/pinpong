package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(String serverAddress, int serverPort) throws IOException {
        //conectar al servidor
        socket = new Socket(serverAddress, serverPort);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    //metodo para enviar mensaje al servidor
    public void send(String message) {
        out.println(message);
    }

    //metodo para recibir mensaje del servidor
    public String receive() {
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return null;
    }

    //
    public void close() throws IOException {
        socket.close();
    }
}