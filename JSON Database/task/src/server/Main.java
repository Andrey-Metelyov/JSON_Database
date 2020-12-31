package server;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        JsonDatabase db = new JsonDatabase();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            String[] command = input.split(" ");
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
                String value = getValue(input);
                if (db.set(index, value)) {
                    System.out.println("OK");
                } else {
                    System.out.println("ERROR");
                }
            }
        }
    }

    private static String getValue(String input) {
        input = input.trim();
        int index = input.indexOf(" "); // first space
        index = input.indexOf(" ", index);
        return input.substring(index);
    }
}
