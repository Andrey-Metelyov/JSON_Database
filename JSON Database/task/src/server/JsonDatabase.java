package server;

import com.google.gson.*;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements Database {
    private final File file = new File(".\\src\\server\\data\\db.json");
    //    private Map<String, JsonElement> data = new HashMap<>();
    private JsonObject data;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public JsonDatabase() {
        readLock.lock();
        Gson gson = new Gson();
        try {
            System.out.println(file.getAbsolutePath());
            data = gson.fromJson(new FileReader(file), JsonObject.class);
            if (data == null) {
                data = new JsonObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            data = new JsonObject();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean set(JsonElement key, JsonElement value) {
        System.out.println("set(" + key + ", " + value + ")");
        writeLock.lock();
        System.out.println(data);
        JsonElement resultElement = null;
        if (key.isJsonPrimitive()) {
            data.add(key.getAsString(), value);
        } else if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            int size = keyArray.size();
            JsonObject current = data;
            for (int i = 0; i < size - 1; i++) {
                String arrayKey = keyArray.get(i).getAsString();
                current = current.get(arrayKey).getAsJsonObject();
            }
            String lastKey = keyArray.get(size - 1).getAsString();
            current.add(lastKey, value);
        }
        System.out.println(data);
        updateFile();
        writeLock.unlock();
        return true;
    }

    private void updateFile() {
//        writeLock.lock();
        try {
            Gson gson = new Gson();
            System.out.println("writing to file: " + data);
            System.out.println(file.getAbsolutePath());
            gson.toJson(data, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            writeLock.unlock();
//        }
    }

    @Override
    public Optional<JsonElement> get(JsonElement key) {
        System.out.println("get(" + key + ")");
        System.out.println("data = " + data);
        Optional<JsonElement> result = Optional.empty();
        readLock.lock();
        JsonElement resultElement = null;
        if (key.isJsonPrimitive()) {
            resultElement = data.get(key.getAsString());
        } else if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            if (keyArray.size() == 1) {
                resultElement = data.get(keyArray.get(0).getAsString());
            } else {
                JsonObject current = data;
                resultElement = null;
                for (JsonElement keyValue : keyArray) {
                    resultElement = current.get(keyValue.getAsString());
                    if (resultElement.isJsonObject()) {
                        current = resultElement.getAsJsonObject();
                    }
                }
            }
        }
        System.out.println("SresultElement = " + resultElement);
        if (resultElement != null) {
            result = Optional.of(resultElement);
        }
        System.out.println("Sresult = " + result);
        readLock.unlock();
        return result;
    }

    @Override
    public boolean delete(JsonElement key) {
//        System.out.println("delete(" + index + ")");
        boolean result = true;
        writeLock.lock();
        System.out.println(data);
        JsonElement resultElement = null;
        if (key.isJsonPrimitive()) {
            data.remove(key.getAsString());
        } else if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            int size = keyArray.size();
            JsonObject current = data;
            for (int i = 0; i < size - 1; i++) {
                String arrayKey = keyArray.get(i).getAsString();
                current = current.get(arrayKey).getAsJsonObject();
            }
            String lastKey = keyArray.get(size - 1).getAsString();
            current.remove(lastKey);
        }
        System.out.println(data);
        writeLock.unlock();
        return result;
    }
}
