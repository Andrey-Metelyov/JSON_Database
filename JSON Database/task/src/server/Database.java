package server;

import java.util.Optional;

public interface Database {
    boolean set(String index, String value);

    Optional<String> get(String index);

    boolean delete(String index);
}
