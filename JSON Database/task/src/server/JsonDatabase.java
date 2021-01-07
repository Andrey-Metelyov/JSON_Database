package server;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements Database {
    private File file = new File(".\\task\\src\\server\\data\\db.json");
    private Map<String, String> data = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public JsonDatabase() {
        readLock.lock();
        try {
//            System.out.println(file.getAbsolutePath());
            FileReader reader = new FileReader(file);
            String content = Files.readString(file.toPath());
//            System.out.println("Read from file:");
//            System.out.println(content);
            Map<String, String> dataFromJson = new Gson().fromJson(content, data.getClass());
            if (dataFromJson != null) {
                data = dataFromJson;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("database init complete");
        System.out.println("loaded " + data.size() + " records");
    }

    @Override
    public boolean set(String index, String value) {
//        System.out.println("set(" + index + "," + value + ")");
        writeLock.lock();
        JsonElement json = JsonParser.parseString(index);
        if (!json.isJsonArray()) {
            System.out.println("(set) Not an array!");
            data.put(index, value);
        } else {
            System.out.println("(set) Array!!!");
            JsonArray array = json.getAsJsonArray();
            System.out.println("array = " + array);
            System.out.println("data = " + data);
            String key = array.get(0).getAsString();
            System.out.println("key = " + key);
            if (data.containsKey(key)) {
                JsonObject valueObject = JsonParser.parseString(data.get(key)).getAsJsonObject();
                System.out.println("valueObject = " + valueObject);
                for (int i = 1; i < array.size() - 1; i++) {
                    key = array.get(i).getAsString();
                    if (valueObject.has(key)) {
                        JsonElement element = valueObject.get(key);
                        if (element.isJsonObject()) {
                            valueObject = element.getAsJsonObject();
                        }
                    }
                }

            }
        }
//        json.
        updateFile();
        writeLock.unlock();
        return true;
    }

    private void updateFile() {
        try {
            FileWriter writer = new FileWriter(file);
            String content = new Gson().toJson(data);
//            System.out.println("Writing to file:");
//            System.out.println(content);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<String> get(String index) {
//        System.out.println("get(" + index + ")");
        Optional<String> result;
        readLock.lock();
        JsonElement json = JsonParser.parseString(index);
        if (!json.isJsonArray()) {
            System.out.println("(get) Not an array!");
            if (data.containsKey(index)) {
                result = Optional.of(data.get(index));
            } else {
                result = Optional.empty();
            }
        } else {
            System.out.println("(get) Array!!!");
            JsonArray array = json.getAsJsonArray();
            System.out.println("array = " + array);
            System.out.println("data = " + data);
            String key = array.get(0).getAsString();
            System.out.println("key = " + key);
            if (data.containsKey(key)) {
                JsonObject value = JsonParser.parseString(data.get(key)).getAsJsonObject();
                System.out.println("value = " + value);
                result = Optional.empty();
                for (int i = 1; i < array.size(); i++) {
                    key = array.get(i).getAsString();
                    if (value.has(key)) {
                        JsonElement element = value.get(key);
                        if (element.isJsonObject()) {
                            value = element.getAsJsonObject();
                            System.out.println("value = " + value);
                            result = Optional.of(value.getAsString());
                        } else {
                            result = Optional.of(element.getAsString());
                        }
                    } else {
                        result = Optional.empty();
                        break;
                    }
                }
            } else {
                result = Optional.empty();
            }
        }
        readLock.unlock();
//        System.out.println("result = " + result);
        return result;
    }

    @Override
    public boolean delete(String index) {
//        System.out.println("delete(" + index + ")");
        boolean result = false;
        writeLock.lock();
        JsonElement json = JsonParser.parseString(index);
        if (!json.isJsonArray()) {
            System.out.println("(delete) Not an array!");
            if (data.containsKey(index)) {
                data.remove(index);
                result = true;
                updateFile();
            }
        } else {
            System.out.println("(delete) Array!!!");
        }
        writeLock.unlock();
        return result;
    }
}
