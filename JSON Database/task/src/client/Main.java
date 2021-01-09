package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.*;
import request.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "-t", description = "The type of the request")
    private String type;

    @Parameter(names = "-k", description = "The index of the cell")
    private String key;

    @Parameter(names = "-v", description = "The value to save in the database")
    private String value;

    @Parameter(names = "-in", description = "Filename to read from")
    private String filename;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
//        main.type = "get";
//        main.index = "1";
//        main.run();
//
//
//        main.type = "get";
//        main.index = "1";
//        main.run();
//
//        main.type = "set";
//        main.index = "name";
//        main.value = "Kate";
//        main.run();
//
//        main.type = "get";
//        main.index = "name";
//        main.run();

//        main.type = "delete";
//        main.index = "name";
//        main.run();

//        main.type = "exit";
/*
        main.type = "set";
        main.key = "1";
        main.value = "HelloWorld!";
/*        main.run();

        main.type = "set";
        main.key = "person";
        main.value = "{\n" +
                "\"name\":\"Elon Musk\",\n" +
                "\"car\":{\n" +
                "   \"model\":\"Tesla Roadster\",\n" +
                "   \"year\":\"2018\"\n" +
                "},\n" +
                "\"rocket\":{\n" +
                "   \"name\":\"Falcon 9\",\n" +
                "   \"launches\":\"87\"\n" +
                "}\n" +
                "}";
        main.run();

        main.type = "get";
        main.key = "['person', 'name']";
        main.run();

        main.type = "set";
        main.key = "['person', 'rocket', 'launches']";
        main.value = "88";
*/
        main.run();
    }

    private void run() throws IOException {
        System.out.println("type: " + type);
        System.out.println("key: " + key);
        System.out.println("value: " + value);

        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 23458;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//        while (true) {
        Gson gson = new Gson();
        JsonObject request;
        String string;
        if (filename != null) {
            String path = ".\\src\\client\\data\\" + filename;
            System.out.println("CRead from file: " + path);
            System.out.println(Path.of(path).toAbsolutePath());
            string = Files.readString(Path.of(path));
            System.out.println("string = " + string);
            request = gson.fromJson(string, JsonObject.class);
//            request = gson.fromJson(content, Request.class);
//            System.out.println(request);
        } else {
            request = new JsonObject();
            switch (type) {
                case "get":
                case "delete":
//                    request = new Request(type, index);
                    request.addProperty("type", type);
                    request.addProperty("key", key);
                    break;
                case "set":
//                    request = new Request(type, index, value);
                    request.addProperty("type", type);
                    request.addProperty("key", key);
                    try {
                        JsonElement gsonValue = JsonParser.parseString(value);
                        System.out.println("gsonValue = " + gsonValue);
                        request.add("value", gsonValue);
                    } catch (JsonSyntaxException e) {
                        System.out.println("value = " + value);
                        request.addProperty("value", value);
                    }
                    System.out.println("Crequest = " + request);
                    break;
                case "exit":
//                    request = new Request(type);
                    request.addProperty("type", type);
                    break;
                default:
                    return;
            }
//            string = gson.toJson(request);
        }
//        output.writeUTF(string);
        output.writeUTF(request.toString());
        System.out.println("Sent: " + request.toString());

        String answer = input.readUTF();
        JsonObject response = new Gson().fromJson(answer, JsonObject.class);
        System.out.println("CReceived: " + answer);
        System.out.println("CReceived: " + response.toString());
        System.out.println("Client closed");
        socket.close();
    }
}
