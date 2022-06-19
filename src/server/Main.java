package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Listening on port 5000");
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new HandleRequest(socket));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
