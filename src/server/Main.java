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

            File file = new File("./src/server/data/" + "map");
            HashMap<Integer, String> map;
            if (file.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    map = (HashMap<Integer, String>) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                map = new HashMap<>();
            }

            System.out.println(map);

            while (true) {
                Socket socket = serverSocket.accept();
                Future<Integer> submit = executorService.submit(new HandleRequest(socket, map));
                Integer o = submit.get();
                if (o == -1) {
                    executorService.shutdown();
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
