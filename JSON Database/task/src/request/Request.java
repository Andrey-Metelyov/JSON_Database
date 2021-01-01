package request;

public class Request {
    String type;
    String key;
    String value;

    public Request(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public Request(String type, String key) {
        this.type = type;
        this.key = key;
    }

    public Request(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
