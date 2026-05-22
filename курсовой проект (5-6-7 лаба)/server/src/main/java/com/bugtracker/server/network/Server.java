package com.bugtracker.server.network;

import com.bugtracker.server.util.HibernateUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;
    private ServerSocket serverSocket;
    private boolean running;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            // инициализируем hibernate при старте
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate инициализирован");

            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Сервер запущен на порту " + port);

            while (running) {
                // ждём подключения клиента
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());

                // создаём отдельный поток для клиента
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            if (running) {
                System.err.println("Ошибка сервера: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            HibernateUtil.shutdown();
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            System.err.println("Ошибка при остановке сервера: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server server = new Server(port);
        server.start();
    }
}