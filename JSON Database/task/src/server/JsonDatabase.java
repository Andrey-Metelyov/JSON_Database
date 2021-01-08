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
    private File file = new File(".\\src\\server\\data\\db.json");
//    private Map<String, JsonElement> data = new HashMap<>();
    private JsonObject data;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public JsonDatabase() {
//        readLock.lock();
//        try {
////            System.out.println(file.getAbsolutePath());
//            FileReader reader = new FileReader(file);
//            String content = Files.readString(file.toPath());
////            System.out.println("Read from file:");
////            System.out.println(content);
//            Map<String, String> dataFromJson = new Gson().fromJson(content, data.getClass());
//            if (dataFromJson != null) {
//                data = dataFromJson;
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            readLock.unlock();
//        }
//        System.out.println("database init complete");
//        System.out.println("loaded " + data.size() + " records");
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
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean set(String key, String value) {
        System.out.println("set(" + key + ", " + value + ")");
        writeLock.lock();
        System.out.println(data);
        data.addProperty(key, value);
        System.out.println(data);
/*        JsonElement jsonIndex = JsonParser.parseString(index);
        if (!jsonIndex.isJsonArray()) {
            System.out.println("(set) Not an array!");
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("value", value);
                System.out.println(jsonObject);
                data.put(index, jsonObject);
//                System.out.println("jsonElement = " + jsonElement);
//                if (data.putIfAbsent(index, jsonElement) != null) {
//                    data.replace(index, jsonElement);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(data);
        } else {
            System.out.println("(set) Array!!!");
            JsonArray array = jsonIndex.getAsJsonArray();
            System.out.println("array = " + array);
            System.out.println("data = " + data);
            String key = array.get(0).getAsString();
            JsonObject jsonObject = data.get(key).getAsJsonObject();
            System.out.println("key = " + key);
            for (int i = 1; i < array.size() - 1; i++) {
                key = array.get(i).getAsString();
                if (jsonObject.has(key)) {
                    JsonElement element = jsonObject.get(key);
                    if (element.isJsonObject()) {
                        jsonObject = element.getAsJsonObject();
                    }
                }
            }
            key = array.get(array.size() - 1).getAsString();
            jsonObject.addProperty(key, value);
            System.out.println("data after modification = " + data);
        }*/
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
    public Optional<String> get(String key) {
//        System.out.println("get(" + index + ")");
        Optional<String> result = Optional.empty();
        readLock.lock();
        JsonElement element = data.get(key);
        if (element != null) {
            result = Optional.of(element.getAsString());
        }
/*        JsonElement json = JsonParser.parseString(index);
        if (!json.isJsonArray()) {
            System.out.println("(get) Not an array!");
            if (data.containsKey(index)) {
                result = Optional.of(data.get(index).getAsString());
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
                JsonObject value = data.get(key).getAsJsonObject();
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
        */
        readLock.unlock();
//        System.out.println("result = " + result);
        return result;
    }

    @Override
    public boolean delete(String index) {
//        System.out.println("delete(" + index + ")");
        boolean result = false;
        writeLock.lock();
//        JsonElement json = JsonParser.parseString(index);
//        if (!json.isJsonArray()) {
//            System.out.println("(delete) Not an array!");
//            if (data.containsKey(index)) {
//                data.remove(index);
//                result = true;
//                updateFile();
//            }
//        } else {
//            System.out.println("(delete) Array!!!");
//        }
        writeLock.unlock();
        return result;
    }
}
