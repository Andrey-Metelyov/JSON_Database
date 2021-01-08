package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import request.Request;
import request.Response;

import java.util.Optional;

public class CommandProcessor {
    private final JsonDatabase db = new JsonDatabase();

    String process(Request input) {
        System.out.println("input = " + input);
        String command = input.getType();
        String result;
        Response response = new Response();

        switch (command) {
            case "get":
                Optional<String> res = db.get(input.getKey());
                if (res.isEmpty()) {
                    result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                    response.setResponse("ERROR");
                    response.setReason("No such key");
                } else {
                    result = "{\"response\":\"OK\",\"value\":\"" + res.get() + "\"}";
                    response.setResponse("OK");
                    response.setValue(res.get());
                }
                break;
            case "delete":
                if (db.delete(input.getKey())) {
                    result = "{\"response\":\"OK\"}";;
                    response.setResponse("OK");
                } else {
                    result = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                    response.setResponse("ERROR");
                    response.setReason("No such key");
                }
                break;
            case "set":
                if (db.set(input.getKey(), input.getValue())) {
                    result = "{\"response\":\"OK\"}";
                    response.setResponse("OK");
                } else {
                    result = "{\"response\":\"ERROR\",\"reason\":\"hz\"}";
                    response.setResponse("ERROR");
                    response.setReason("hz");
                }
                break;
            case "exit":
                response.setResponse("OK");
                break;
            default:
                result = "{\"response\":\"ERROR\",\"reason\":\"hz\"}";
                response.setResponse("ERROR");
                response.setReason("hz");
                break;
        }

        return new Gson().toJson(response);
    }

    public static void main(String[] args) {
        JsonObject obj = new JsonObject();
        JsonObject rocket = new JsonObject();

        rocket.addProperty("name", "Falcon 9");
        rocket.addProperty("launches", "88");

        obj.addProperty("type", "set");
        obj.addProperty("key", "name");
        obj.addProperty("value", "Kate");
        obj.add("rocket", rocket);
        System.out.println(obj.get("rocket"));
        System.out.println(obj.get("['rocket','launches']"));

        System.out.println(obj);
    }
}
