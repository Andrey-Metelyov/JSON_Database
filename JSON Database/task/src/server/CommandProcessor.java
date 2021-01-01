package server;

import java.util.Optional;

public class CommandProcessor {
    private final JsonDatabase db = new JsonDatabase();

    String process(String input) {
        String[] command = input.split(" ", 3);
        String result;

        switch (command[0]) {
            case "get":
                Optional<String> res = db.get(Integer.parseInt(command[1]));
                if (res.isEmpty()) {
                    result = "ERROR";
                } else {
                    result = res.get();
                }
                break;
            case "delete":
                if (db.delete(Integer.parseInt(command[1]))) {
                    result = "OK";
                } else {
                    result = "ERROR";
                }
                break;
            case "set":
                int index = Integer.parseInt(command[1]);
                String value = command[2];
                if (db.set(index, value)) {
                    result = "OK";
                } else {
                    result = "ERROR";
                }
                break;
            default:
                result = "ERROR";
                break;
        }

        return result;
    }
}
