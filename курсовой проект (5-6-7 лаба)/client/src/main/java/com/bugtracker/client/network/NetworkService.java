package com.bugtracker.client.network;

import com.bugtracker.common.dto.Request;
import com.bugtracker.common.dto.Response;
import com.bugtracker.common.enums.ActionType;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;

public class NetworkService {

    private static NetworkService instance;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final Gson gson = new Gson();

    private String host = "localhost";
    private int port = 8080;

    // приватный конструктор для синглтона
    private NetworkService() {}

    // получить единственный экземпляр
    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    // подключение к серверу
    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Подключено к серверу " + host + ":" + port);
    }

    // подключение с параметрами
    public void connect(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    // отключение
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Отключено от сервера");
        } catch (IOException e) {
            System.err.println("Ошибка при отключении: " + e.getMessage());
        }
    }

    // отправить запрос и получить ответ
    public Response sendRequest(ActionType action, String data) {
        try {
            Request request = new Request(action, data);
            String jsonRequest = gson.toJson(request);

            out.println(jsonRequest);
            String jsonResponse = in.readLine();

            return gson.fromJson(jsonResponse, Response.class);
        } catch (IOException e) {
            return new Response(false, "Ошибка сети: " + e.getMessage(), null);
        }
    }

    // отправить запрос без данных
    public Response sendRequest(ActionType action) {
        return sendRequest(action, null);
    }

    // проверка подключения
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}