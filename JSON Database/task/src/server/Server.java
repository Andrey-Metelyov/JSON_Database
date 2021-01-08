package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private String address = "127.0.0.1";
    private int port = 23458;
    private ServerSocket server;

    void start() throws IOException {
        server = new ServerSocket(port, 50, InetAddress.getByName(address));
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        CommandProcessor commandProcessor = new CommandProcessor();
        System.out.println("Server started!");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = server.accept();
                System.out.println("New connection: " + socket);
                threadPool.submit(new ClientHandler(socket, commandProcessor, this));
            }
        } finally {
            threadPool.shutdown();
            server.close();
            System.out.println("Server closed!");
        }
    }

    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
