package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import netscape.javascript.JSException;
import request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final CommandProcessor commandProcessor;
    private final Server server;

    public ClientHandler(Socket socket, CommandProcessor commandProcessor, Server server) {
        this.socket = socket;
        this.commandProcessor = commandProcessor;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String command = input.readUTF();

            System.out.println("Received: " + command);
            Request request = new Gson().fromJson(command, Request.class);

            String answer = commandProcessor.process(request);

            output.writeUTF(answer);
            System.out.println("Sent: " + answer);
            System.out.println("Connection closing: " + socket);
            socket.close();
            if (request.getType().equals("exit")) {
                server.stop();
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
