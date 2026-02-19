package com.server.network;

import com.server.model.entities.User;
import com.server.repositories.UserDAO;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

public class Server {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("server");
        int serverPort = Integer.parseInt(bundle.getString("SERVER_PORT"));
        System.out.println("Сервер запускается....");

        UserDAO userDAO = new UserDAO();
        if (userDAO.findByLogin("admin") == null) {
            try {
                String hashedPassword = java.util.Base64.getEncoder().encodeToString(
                        java.security.MessageDigest.getInstance("SHA-256").digest("admin".getBytes())
                );
                userDAO.create(new User(1, "admin", hashedPassword, 3));
                System.out.println("Админ-аккаунт создан (admin/admin)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Сервер запущен на порте " + serverPort + "!");
            while (true) {
                System.out.println("Ожидание подключения....");
                Socket clientAccepted = serverSocket.accept();
                System.out.println("Соединение установлено...");
                new ClientThread(clientAccepted).run();
                System.out.println("Соединение потеряно...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}