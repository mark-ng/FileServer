package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String userAction = scanner.nextLine();
        switch (userAction) {
            case "1":
                System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
                String getByNameOrId = scanner.nextLine();
                switch (getByNameOrId) {
                    case "1":
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        getFile("BY_NAME", name, scanner);
                        break;
                    case "2":
                        System.out.print("Enter id: ");
                        String id = scanner.nextLine();
                        getFile("BY_ID", id, scanner);
                        break;
                }
                break;
            case "2":
                System.out.print("Enter name of the file: ");
                String filename = scanner.nextLine();
                System.out.print("Enter name of the file to be saved on server: ");
                String newFilename = scanner.nextLine();
                saveFile(filename, newFilename.equals("") ? filename : newFilename);
                break;
            case "3":
                System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
                String deleteByNameOrId = scanner.nextLine();
                switch (deleteByNameOrId) {
                    case "1":
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        break;
                    case "2":
                        System.out.print("Enter id: ");
                        String id = scanner.nextLine();
                        break;
                }
                break;
            case "exit":
                break;
        }
    }

    public static void saveFile(String filename, String newFilename) {
        File file = new File("./src/client/data/" + filename);
        if (!file.exists()) {
            System.out.println("Fail to save file!");
            return;
        }

        try (Socket socket = new Socket("localhost", 5000)) {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream.writeUTF("PUT" + " " + newFilename);

            // Send File To Server
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                // Send the length of the file
                dataOutputStream.writeInt((int) file.length());
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                // Read from file to buffer
                fileInputStream.read(bytes, 0, length);
                // Write from buffer to socket
                dataOutputStream.write(bytes, 0, length);
            }

            System.out.println("The request was sent.");

            String serverMsg = dataInputStream.readUTF();
            String[] serverResponse = serverMsg.split(" ");
            String statusCode = serverResponse[0];
            if (statusCode.equals("200")) {
                String fileLabel = serverResponse[1];
                System.out.println("Response says that file is saved! ID = " + fileLabel);
            } else if (statusCode.equals("403")) {
                System.out.println("Fail to save file!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getFile(String byIdOrName, String fileLabel, Scanner scanner) {
        try (Socket socket = new Socket("localhost", 5000);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ) {
            String requestMsg = "GET" + " " + byIdOrName + " " + fileLabel;
            System.out.println(requestMsg);
            dataOutputStream.writeUTF(requestMsg);

            System.out.println("The request was sent.");

            System.out.print("The file was downloaded! Specify a name for it: ");
            String newFilename = scanner.nextLine();
            int size = dataInputStream.readInt();
            byte[] bytes = new byte[size];
            dataInputStream.readFully(bytes, 0, size);
            File file = new File("./src/client/data/" + newFilename);

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes, 0, size);
            }

            System.out.println("File saved on the hard drive!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
