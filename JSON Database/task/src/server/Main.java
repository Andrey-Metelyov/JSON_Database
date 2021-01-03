package server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
//        System.out.println("Server started!");
//
//        String address = "127.0.0.1";
//        int port = 23458;
//        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
//        ExecutorService threadPool = Executors.newFixedThreadPool(4);
//        CommandProcessor commandProcessor = new CommandProcessor();
//        try {
//            while (!Thread.currentThread().isInterrupted()) {
//                Socket socket = server.accept();
//                threadPool.submit(new ClientHandler(socket, commandProcessor));
//            }
//        } finally {
//            threadPool.shutdown();
//        }
//        while (true) {
//            Socket socket = server.accept();
//            DataInputStream input = new DataInputStream(socket.getInputStream());
//            DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
//            String command = input.readUTF();
//            Gson gson = new Gson();
//            Request request = gson.fromJson(command, Request.class);
//            if (request.getType().equals("exit")) {
//                output.writeUTF("{\"response\":\"OK\"}");
//                break;
//            }
//            System.out.println("Received: " + command);
//            String answer = commandProcessor.process(request);
//            output.writeUTF(answer);
//            System.out.println("Sent: " + answer);
//            socket.close();
//        }
//        server.close();
//        System.out.println("Server closed!");
    }
}
