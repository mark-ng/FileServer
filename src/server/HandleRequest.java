package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Callable;

public class HandleRequest implements Callable {

    private final Socket socket;
    private final Map<Integer, String> map;

    public HandleRequest(Socket socket, Map<Integer, String> map) {
        this.socket = socket;
        this.map = map;
    }

    @Override
    public Integer call() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String requestMessage = dataInputStream.readUTF();
            System.out.println(requestMessage);
            String[] request = requestMessage.split(" ");
            String requestMethod = request[0];

            switch (requestMethod) {
                case "exit":
                    return -1;
                case "PUT":
                    String newFilename = request[1];
                    try (FileOutputStream fileOutputStream = new FileOutputStream("./src/server/data/" + newFilename)) {
                        int size = dataInputStream.readInt();
                        byte[] bytes = new byte[size];
                        dataInputStream.readFully(bytes, 0, size);
                        fileOutputStream.write(bytes, 0, size);
                    } catch (IOException e) {
                        String responseMsg = "403";
                        dataOutputStream.writeUTF(responseMsg);
                    }

                    // Generate integer identifier
                    Random r = new Random();
                    int randomInt = r.nextInt(10000);
                    while (map.containsKey(randomInt)) {
                        randomInt = r.nextInt(10000);
                    }


                    map.put(randomInt, newFilename);

                    try (FileOutputStream mapOutputStream = new FileOutputStream("./src/server/data/" + "map")) {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mapOutputStream);
                        objectOutputStream.writeObject(map);
                    }

                    System.out.println(map);

                    String responseMsg = "200" + " " + randomInt;
                    dataOutputStream.writeUTF(responseMsg);
                    break;
                case "GET":
                    String byIdOrName = request[1];
                    String fileLabel = request[2];
                    if (byIdOrName.equals("BY_NAME")) {
                        File file = new File("./src/server/data/" + fileLabel);
                        if (!file.exists()) {
                            dataOutputStream.writeUTF("404");
                            break;
                        }
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            dataOutputStream.writeUTF("200");
                            int size = (int) file.length();
                            dataOutputStream.writeInt(size);
                            byte[] bytes = new byte[(int) size];
                            fileInputStream.read(bytes, 0, size);
                            dataOutputStream.write(bytes, 0, size);
                        }
                    } else if (byIdOrName.equals("BY_ID")) {
                        Integer fileNumber = Integer.valueOf(fileLabel);
                        // If file exist
                        if (map.containsKey(fileNumber)) {
                            String filename = map.get(fileNumber);
                            File file = new File("./src/server/data/" + filename);
                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                dataOutputStream.writeUTF("200");
                                int size = (int) file.length();
                                dataOutputStream.writeInt(size);
                                byte[] bytes = new byte[(int) size];
                                fileInputStream.read(bytes, 0, size);
                                dataOutputStream.write(bytes, 0, size);
                            }
                        } else {
                            dataOutputStream.writeUTF("404");
                        }
                    }

                    break;
                case "DELETE":
                    String deleteByIdOrName = request[1];
                    String deleteFileLabel = request[2];
                    if (deleteByIdOrName.equals("BY_NAME")) {
                        File file = new File("./src/server/data/" + deleteFileLabel);
                        if (file.exists()) {
                            boolean deleted = file.delete();
                            if (deleted) {
                                dataOutputStream.writeUTF("200");
                            } else {
                                dataOutputStream.writeUTF("404");
                            }
                        } else {
                            dataOutputStream.writeUTF("404");
                        }
                    } else if (deleteByIdOrName.equals("BY_ID")) {
                        Integer fileInt = Integer.valueOf(deleteFileLabel);
                        if (map.containsKey(fileInt)) {
                            String filename = map.get(fileInt);
                            System.out.println("Delete: " + filename);
                            File file = new File("./src/server/data/" + filename);
                            if (file.exists()) {
                                boolean deleted = file.delete();
                                if (deleted) {
                                    dataOutputStream.writeUTF("200");
                                } else {
                                    System.out.println("cannot delete");
                                    dataOutputStream.writeUTF("404");
                                }
                            } else {
                                dataOutputStream.writeUTF("404");
                            }
                        } else {
                            dataOutputStream.writeUTF("404");
                        }
                    }
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

        return 0;
    }
}
