package server;

import java.util.Optional;

public interface Database {
    boolean set(int index, String value);

    Optional<String> get(int index);

    boolean delete(int index);
}
