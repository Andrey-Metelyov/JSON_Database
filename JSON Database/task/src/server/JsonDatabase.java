package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonDatabase implements Database {
    private Map<String, String> data = new HashMap<>();

    public JsonDatabase() {
    }

    @Override
    public boolean set(String index, String value) {
        data.put(index, value);
        return true;
    }

    @Override
    public Optional<String> get(String index) {
        if (data.containsKey(index)) {
            return Optional.of(data.get(index));
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(String index) {
        if (data.containsKey(index)) {
            data.remove(index);
            return true;
        }
        return false;
    }
}
