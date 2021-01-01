package server;

import java.util.Optional;
import java.util.Scanner;

public class CommandProcessor {
    private JsonDatabase db = new JsonDatabase();
    private Scanner scanner = new Scanner(System.in);

    void run() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            String[] command = input.split(" ", 3);
            if (command[0].equals("get")) {
                Optional<String> res = db.get(Integer.parseInt(command[1]));
                if (res.isEmpty()) {
                    System.out.println("ERROR");
                } else {
                    System.out.println(res.get());
                }
            } else if (command[0].equals("delete")) {
                if (db.delete(Integer.parseInt(command[1]))) {
                    System.out.println("OK");
                } else {
                    System.out.println("ERROR");
                }
            } else if (command[0].equals("set")) {
                int index = Integer.parseInt(command[1]);
                String value = command[2];
                if (db.set(index, value)) {
                    System.out.println("OK");
                } else {
                    System.out.println("ERROR");
                }
            }
        }
    }
}
