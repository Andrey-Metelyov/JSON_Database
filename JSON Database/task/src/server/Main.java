package server;

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
            if (command.equals("exit")) {
                output.writeUTF("OK");
                break;
            }
            System.out.println("Received: " + command);
            String answer = commandProcessor.process(command);
            output.writeUTF(answer);
            System.out.println("Sent: " + answer);
            socket.close();
        }
        server.close();
        System.out.println("Server closed!");
    }
}
