package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import request.Request;
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
    private String index;

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
//        main.type = "set";
//        main.index = "1";
//        main.value = "HelloWorld!";
//        main.run();
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
        main.type = "set";
        main.index = "person";
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
        main.index = "['person', 'name']";
        main.run();

        main.type = "set";
        main.index = "['person', 'rocket', 'launches']";
        main.value = "88";
        main.run();
    }

    private void run() throws IOException {
//        System.out.println(type);
//        System.out.println(index);
//        System.out.println(value);

        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 23458;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//        while (true) {
        Gson gson = new Gson();
        Request request;
        if (filename != null) {
            String path = ".\\src\\client\\data\\" + filename;
            System.out.println("Read from file: " + path);
            System.out.println(Path.of(path).toAbsolutePath());
            String content = Files.readString(Path.of(path));
            request = gson.fromJson(content, Request.class);
        } else {
            switch (type) {
                case "get":
                case "delete":
                    request = new Request(type, index);
                    break;
                case "set":
                    request = new Request(type, index, value);
                    break;
                case "exit":
                    request = new Request(type);
                    break;
                default:
                    return;
            }
        }
        String string = gson.toJson(request);
        output.writeUTF(string);
        System.out.println("Sent: " + string);

        String answer = input.readUTF();
        Response response = new Gson().fromJson(answer, Response.class);
        System.out.println("Received: " + answer);
//        System.out.println("Received: " + response);
        socket.close();
    }
}
