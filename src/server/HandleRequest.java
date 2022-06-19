package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class HandleRequest implements Runnable {

    private final Socket socket;
    private final Map<Integer, String> map;

    public HandleRequest(Socket socket, Map<Integer, String> map) {
        this.socket = socket;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String requestMessage = dataInputStream.readUTF();
            System.out.println(requestMessage);
            String[] request = requestMessage.split(" ");
            String requestMethod = request[0];

            switch (requestMethod) {
                case "PUT":
                    String newFilename = request[1];
                    try (FileOutputStream fileOutputStream = new FileOutputStream("./src/server/data/" + newFilename)) {
                        int size = dataInputStream.readInt();
                        byte[] bytes = new byte[size];
                        dataInputStream.readFully(bytes, 0, size);
                        fileOutputStream.write(bytes, 0, size);
                    }

                    // Generate integer identifier
                    Random r = new Random();
                    int randomInt = r.nextInt(10000);
                    while (map.containsKey(randomInt)) {
                        randomInt = r.nextInt(10000);
                    }
                    map.put(randomInt, newFilename);
                    dataOutputStream.writeUTF("Response says that file is saved! ID = " + randomInt);
                    break;
                case "GET":
                    break;
                case "DELETE":
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
