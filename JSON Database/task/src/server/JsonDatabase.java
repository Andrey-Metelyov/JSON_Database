package server;

import java.util.Arrays;
import java.util.Optional;

public class JsonDatabase implements Database {
    private String[] data = new String[100];

    public JsonDatabase() {
        Arrays.fill(data, "");
    }

    @Override
    public boolean set(int index, String value) {
        if (index > data.length) {
            return false;
        }
        data[index - 1] = value;
        return true;
    }

    @Override
    public Optional<String> get(int index) {
        return (index > data.length || data[index - 1].isEmpty()) ? Optional.empty() : Optional.of(data[index - 1]);
    }

    @Override
    public boolean delete(int index) {
        if (index > data.length) {
            return false;
        }
        data[index - 1] = "";
        return true;
    }
}
