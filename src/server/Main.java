package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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
