package server;

import com.google.gson.Gson;
import request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Server started!");

        String address = "127.0.0.1";
        int port = 23458;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        CommandProcessor commandProcessor = new CommandProcessor();
        while (true) {
            Socket socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
            String command = input.readUTF();
            Gson gson = new Gson();
            Request request = gson.fromJson(command, Request.class);
            if (request.getType().equals("exit")) {
                output.writeUTF("{\"response\":\"OK\"}");
                break;
            }
            System.out.println("Received: " + command);
            String answer = commandProcessor.process(request);
            output.writeUTF(answer);
            System.out.println("Sent: " + answer);
            socket.close();
        }
        server.close();
        System.out.println("Server closed!");
    }
}
