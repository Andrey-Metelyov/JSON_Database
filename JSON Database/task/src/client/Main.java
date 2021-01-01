package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    private void run() throws IOException {
        System.out.println(type);
        System.out.println(index);
        System.out.println(value);

        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 23458;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//        while (true) {
        Gson gson = new Gson();
        Request request;
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
        String string = gson.toJson(request);
        output.writeUTF(string);
        System.out.println("Sent: " + string);

        String answer = input.readUTF();
        System.out.println("Received: " + answer);
        socket.close();
    }
}
