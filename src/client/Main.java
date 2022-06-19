package client;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String userAction = scanner.nextLine();
        switch(userAction) {
            case "1":
                System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
                String getByNameOrId = scanner.nextLine();
                switch (getByNameOrId) {
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
            case "2":
                System.out.print("Enter name of the file: ");
                String filename = scanner.nextLine();
                System.out.print("Enter name of the file to be saved on server: ");
                String newFilename = scanner.nextLine();
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

}
