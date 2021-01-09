package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public interface Database {
    boolean set(JsonElement key, JsonElement value);

    Optional<JsonElement> get(JsonElement key);

    boolean delete(JsonElement key);
}
