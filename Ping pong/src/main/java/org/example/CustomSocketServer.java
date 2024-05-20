package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomSocketServer {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private List<PrintWriter> clientOutputs = new ArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(5);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            executorService.submit(() -> handleClient(clientSocket));
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientOutputs.add(out);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("RECEIVED FROM CLIENT: " + inputLine);
                for (PrintWriter writer : clientOutputs) {
                    writer.println(inputLine);
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        executorService.shutdown();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        CustomSocketServer server = new CustomSocketServer();
        server.start(6666);
        server.stop();
    }
}