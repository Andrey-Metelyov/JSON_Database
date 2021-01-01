package server;

import com.google.gson.Gson;
import request.Request;

import java.util.Optional;

public class CommandProcessor {
    private final JsonDatabase db = new JsonDatabase();

    String process(Request input) {
        String command = input.getType();
        String result;

        switch (command) {
            case "get":
                Optional<String> res = db.get(input.getKey());
                if (res.isEmpty()) {
                    result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                } else {
                    result = "{\"response\":\"OK\",\"value\":\"" + res.get() + "\"}";
                }
                break;
            case "delete":
                if (db.delete(input.getKey())) {
                    result = "{\"response\":\"OK\"}";;
                } else {
                    result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                }
                break;
            case "set":
                if (db.set(input.getKey(), input.getValue())) {
                    result = "{\"response\":\"OK\"}";
                } else {
                    result = "{\"response\":\"ERROR\",\"reason\":\"hz\"}";
                }
                break;
            default:
                result = "{\"response\":\"ERROR\",\"reason\":\"hz\"}";
                break;
        }

        return result;
    }
}
