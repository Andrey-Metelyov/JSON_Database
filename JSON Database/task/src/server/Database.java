package server;

import java.util.Optional;

public interface Database {
    boolean set(String key, String value);

    Optional<String> get(String key);

    boolean delete(String key);
}
