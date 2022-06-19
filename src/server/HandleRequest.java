package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandleRequest implements Runnable {

    private final Socket socket;

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
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
