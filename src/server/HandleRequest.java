package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class HandleRequest implements Runnable {

    private Socket socket;

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            String s = dataInputStream.readUTF();
            System.out.println(s);
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
