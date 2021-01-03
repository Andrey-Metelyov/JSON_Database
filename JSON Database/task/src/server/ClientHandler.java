package server;

import com.google.gson.Gson;
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

            Request request = new Gson().fromJson(command, Request.class);
            System.out.println("Received: " + command);

            String answer = commandProcessor.process(request);

            output.writeUTF(answer);
            System.out.println("Sent: " + answer);
            socket.close();
            if (request.getType().equals("exit")) {
                server.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
