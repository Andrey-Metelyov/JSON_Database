package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public class CommandProcessor {
    private final JsonDatabase db = new JsonDatabase();

    String process(JsonObject request) {
        System.out.println("Sinput = " + request);
        String command = request.get("type").getAsString();
        JsonElement key = request.get("key");
        JsonObject response = new JsonObject();

        switch (command) {
            case "get":
                Optional<JsonElement> res = db.get(key);
                if (res.isEmpty()) {
                    response.addProperty("response", "ERROR");
                    response.addProperty("reason", "No such key");
                } else {
                    response.addProperty("response", "OK");
                    response.add("value", res.get());
                }
                break;
            case "delete":
                if (db.delete(key)) {
                    response.addProperty("response", "OK");
                } else {
                    response.addProperty("response", "ERROR");
                    response.addProperty("reason", "No such key");
                }
                break;
            case "set":
                JsonElement value = request.get("value");
                if (db.set(key, value)) {
                    response.addProperty("response", "OK");
                } else {
                    response.addProperty("response", "ERROR");
                    response.addProperty("reason", "hz");
                }
                break;
            case "exit":
                response.addProperty("response", "OK");
                break;
            default:
                response.addProperty("response", "ERROR");
                response.addProperty("reason", "hz");
                break;
        }
        String answer = new Gson().toJson(response);
        System.out.println("SAnswer: " + answer);
        return answer;
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
