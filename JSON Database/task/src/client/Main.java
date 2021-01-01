package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Client started!");

        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//        while (true) {
            String request = "Give me a record # 12";
            output.writeUTF(request);
            System.out.println("Sent: " + request);

            String answer = input.readUTF();
            System.out.println("Received: " + answer);
//        }
    }
}
