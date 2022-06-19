package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Listening on port 5000");
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            HashMap<Integer, String> map = new HashMap<>();

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new HandleRequest(socket, map));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
