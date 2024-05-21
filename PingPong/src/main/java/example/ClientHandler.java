package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private CustomSocketServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String name;

    public ClientHandler(Socket socket, CustomSocketServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            out.println("Por favor, introduce tu nombre:");
            name = in.readLine();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                server.broadcastMessage(inputLine, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getName() {
        return name;
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}