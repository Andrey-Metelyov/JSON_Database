package server;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase implements Database {
    private File file = new File("db.json");
    private Map<String, String> data = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public JsonDatabase() {
        readLock.lock();
        try {
            FileReader reader = new FileReader(file);
            String content = Files.readString(file.toPath());
            System.out.println("Read from file:");
            System.out.println(content);
            data = new Gson().fromJson(content, data.getClass());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("database init complete");
    }

    @Override
    public boolean set(String index, String value) {
        System.out.println("set(" + index + "," + value + ")");
        writeLock.lock();
        data.put(index, value);
        updateFile();
        writeLock.unlock();
        return true;
    }

    private void updateFile() {
        try {
            FileWriter writer = new FileWriter(file);
            String content = new Gson().toJson(data);
            System.out.println("Writing to file:");
            System.out.println(content);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<String> get(String index) {
        System.out.println("get(" + index + ")");
        Optional<String> result;
        readLock.lock();
        if (data.containsKey(index)) {
            result = Optional.of(data.get(index));
        } else {
            result = Optional.empty();
        }
        readLock.unlock();
        System.out.println("result = " + result);
        return result;
    }

    @Override
    public boolean delete(String index) {
        System.out.println("delete(" + index + ")");
        boolean result = false;
        writeLock.lock();
        if (data.containsKey(index)) {
            data.remove(index);
            result = true;
            updateFile();
        }
        writeLock.unlock();
        return result;
    }
}
