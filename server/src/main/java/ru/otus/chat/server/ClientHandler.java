package ru.otus.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String username;
    private static int userCount = 0;

    public String getUsername() {
        return username;
    }

    public ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        String confirmation = "Клиент подключился";
        out.writeUTF(confirmation);
        userCount++;
        String nameRequest = "Введите никнейм";
        out.writeUTF(nameRequest);
        username = in.readUTF();
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.startsWith("/exit")){
                            sendMessage("/exitok");
                            break;
                        }
                        if (message.startsWith("/w ")) {
                            message = message.replace("/w ", "");
                            for (ClientHandler client : server.getClients()) {
                                if (message.startsWith(client.username)) {
                                    message = message.replace(client.username, "");
                                    client.sendMessage(username + " : " +  message);
                                    receiveMessage();
                                }
                            }
                        }
                    } else {
                        server.broadcastMessage(username + " : " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        try {
            in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        server.unsubscribe(this);
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
