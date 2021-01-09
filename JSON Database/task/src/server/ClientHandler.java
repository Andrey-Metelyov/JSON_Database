package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

            System.out.println("SReceived: " + command);
            JsonObject request = new Gson().fromJson(command, JsonObject.class);
            System.out.println(request);
            String type = request.get("type").getAsString();

            String answer = commandProcessor.process(request);

            output.writeUTF(answer);
            System.out.println("SSent: " + answer);
            System.out.println("SConnection closing: " + socket);
            socket.close();
            if (type.equals("exit")) {
                server.stop();
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
