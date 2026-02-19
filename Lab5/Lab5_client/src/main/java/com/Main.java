package com;

import com.server.enums.Operation;
import com.server.model.entities.Game;
import com.server.model.entities.User;
import com.server.network.Request;
import com.server.network.Response;
import com.server.network.ServerClient;
import com.server.serializer.Deserializer;
import com.server.serializer.Serializer;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static User currentUser = null;

    public static void main(String[] args) {
        try {
            ServerClient client = ServerClient.getInstance();
            Scanner sc = new Scanner(System.in);
            boolean running = true;

            while (running) {
                if (currentUser == null) {
                    showAuthMenu();
                } else {
                    showMainMenu();
                }

                int choice = sc.nextInt();
                sc.nextLine();

                Response response;

                if (currentUser == null) {
                    switch (choice) {
                        case 1 -> {
                            System.out.print("Логин: ");
                            String login = sc.nextLine();
                            System.out.print("Пароль: ");
                            String password = sc.nextLine();
                            response = client.sendRequest(new Request(Operation.LOGIN, login + ":" + password));
                            printResponse(response, User.class);
                            if (response.isSuccess() && response.getData() != null) {
                                currentUser = Deserializer.deserialize(response.getData(), User.class);
                                System.out.println("Добро пожаловать, " + currentUser.getUsername() + "!");
                            }
                        }
                        case 2 -> {
                            System.out.print("Логин: ");
                            String login = sc.nextLine();
                            System.out.print("Пароль: ");
                            String password = sc.nextLine();
                            System.out.println("Выберите роль:");
                            System.out.println("  1 - Пользователь");
                            System.out.println("  2 - Разработчик");
                            System.out.print("Роль: ");
                            int roleId = sc.nextInt();
                            sc.nextLine();
                            if (roleId != 1 && roleId != 2) {
                                System.out.println("Неверная роль!");
                                break;
                            }
                            String data = login + ":" + password + ":" + roleId;
                            response = client.sendRequest(new Request(Operation.REGISTER, data));
                            printResponse(response, User.class);
                            if (response.isSuccess() && response.getData() != null) {
                                currentUser = Deserializer.deserialize(response.getData(), User.class);
                                System.out.println("Добро пожаловать, " + currentUser.getUsername() + "!");
                            }
                        }
                        case 0 -> {
                            client.sendRequest(new Request(Operation.DISCONNECT));
                            client.disconnect();
                            running = false;
                            System.out.println("До свидания!");
                        }
                        default -> System.out.println("Неверный выбор!");
                    }
                } else {
                    switch (choice) {
                        case 1 -> {
                            response = client.sendRequest(new Request(Operation.GET_ALL_GAMES));
                            printResponse(response, Game.class);
                        }
                        case 2 -> {
                            System.out.print("Введите ID игры: ");
                            String id = sc.nextLine();
                            response = client.sendRequest(new Request(Operation.READ_GAME_DATA, id));
                            printResponse(response, Game.class);
                        }
                        case 3 -> {
                            response = client.sendRequest(new Request(Operation.GET_ALL_DEVELOPERS));
                            printResponse(response, null);
                        }
                        case 4 -> {
                            System.out.print("Введите ID игры: ");
                            String id = sc.nextLine();
                            response = client.sendRequest(new Request(Operation.GET_DEVELOPERS_BY_GAME, id));
                            printResponse(response, null);
                        }
                        case 5 -> {
                            if (currentUser.getRoleId() < 2) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            System.out.print("ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Название: ");
                            String title = sc.nextLine();
                            System.out.print("Жанр: ");
                            String genre = sc.nextLine();
                            System.out.print("Цена: ");
                            double price = sc.nextDouble();
                            System.out.print("Год выхода: ");
                            int year = sc.nextInt();
                            System.out.print("Рейтинг (0-10): ");
                            double rating = sc.nextDouble();
                            sc.nextLine();
                            response = client.sendRequest(new Request(Operation.CREATE_GAME,
                                    Serializer.serialize(new Game(id, title, genre, price, year, rating))));
                            printResponse(response, null);
                        }
                        case 6 -> {
                            if (currentUser.getRoleId() < 2) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            System.out.print("ID игры: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Новое название: ");
                            String title = sc.nextLine();
                            System.out.print("Новый жанр: ");
                            String genre = sc.nextLine();
                            System.out.print("Новая цена: ");
                            double price = sc.nextDouble();
                            System.out.print("Новый год выхода: ");
                            int year = sc.nextInt();
                            System.out.print("Новый рейтинг (0-10): ");
                            double rating = sc.nextDouble();
                            sc.nextLine();
                            response = client.sendRequest(new Request(Operation.UPDATE_GAME,
                                    Serializer.serialize(new Game(id, title, genre, price, year, rating))));
                            printResponse(response, null);
                        }
                        case 7 -> {
                            if (currentUser.getRoleId() < 2) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            System.out.print("ID игры для удаления: ");
                            String id = sc.nextLine();
                            response = client.sendRequest(new Request(Operation.DELETE_GAME, id));
                            printResponse(response, null);
                        }
                        case 8 -> {
                            if (currentUser.getRoleId() != 3) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            response = client.sendRequest(new Request(Operation.GET_ALL_USERS));
                            printResponse(response, User.class);
                        }
                        case 9 -> {
                            if (currentUser.getRoleId() != 3) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            response = client.sendRequest(new Request(Operation.GET_ALL_ROLES));
                            printResponse(response, null);
                        }
                        case 10 -> {
                            if (currentUser.getRoleId() != 3) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            System.out.print("ID игры: ");
                            int gameId = sc.nextInt();
                            System.out.print("ID разработчика: ");
                            int devId = sc.nextInt();
                            sc.nextLine();
                            response = client.sendRequest(new Request(Operation.JOIN_DEVELOPER_GAME, gameId + ":" + devId));
                            printResponse(response, null);
                        }
                        case 11 -> {
                            if (currentUser.getRoleId() != 3) {
                                System.out.println("Недостаточно прав!");
                                break;
                            }
                            System.out.print("ID игры: ");
                            int gameId = sc.nextInt();
                            System.out.print("ID разработчика: ");
                            int devId = sc.nextInt();
                            sc.nextLine();
                            response = client.sendRequest(new Request(Operation.SEPARATE_DEVELOPER_GAME, gameId + ":" + devId));
                            printResponse(response, null);
                        }
                        case 12 -> {
                            currentUser = null;
                            System.out.println("Вы вышли из аккаунта.");
                        }
                        case 0 -> {
                            client.sendRequest(new Request(Operation.DISCONNECT));
                            client.disconnect();
                            running = false;
                            System.out.println("До свидания!");
                        }
                        default -> System.out.println("Неверный выбор!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Не удалось подключиться к серверу!");
            e.printStackTrace();
        }
    }

    private static void showAuthMenu() {
        System.out.println("\n=== GameStore67 ===");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void showMainMenu() {
        String role = currentUser.getRoleId() == 3 ? "ADMIN" :
                currentUser.getRoleId() == 2 ? "DEVELOPER" : "USER";
        System.out.println("\n=== GameStore67 [" + currentUser.getUsername() + " | " + role + "] ===");
        System.out.println("--- Просмотр ---");
        System.out.println("1. Показать все игры");
        System.out.println("2. Найти игру по ID");
        System.out.println("3. Показать всех разработчиков");
        System.out.println("4. Показать разработчиков игры");

        if (currentUser.getRoleId() >= 2) {
            System.out.println("--- Управление играми ---");
            System.out.println("5. Добавить игру");
            System.out.println("6. Обновить игру");
            System.out.println("7. Удалить игру");
        }

        if (currentUser.getRoleId() == 3) {
            System.out.println("--- Администрирование ---");
            System.out.println("8. Показать всех пользователей");
            System.out.println("9. Показать все роли");
            System.out.println("10. Привязать разработчика к игре");
            System.out.println("11. Отвязать разработчика от игры");
        }

        System.out.println("--- ---");
        System.out.println("12. Выйти из аккаунта");
        System.out.println("0. Выход из программы");
        System.out.print("Выберите действие: ");
    }

    private static void printResponse(Response response, Class<?> type) {
        System.out.println("\n--- Ответ сервера ---");
        System.out.println("Успех: " + response.isSuccess());
        System.out.println("Сообщение: " + response.getMessage());

        if (response.getData() == null || response.getData().isEmpty()) {
            return;
        }

        String data = response.getData();

        if (type == null) {
            System.out.println("Данные: " + data);
            return;
        }

        try {
            if (data.trim().startsWith("[")) {
                List<?> list = Deserializer.deserializeList(data, type);
                System.out.println("Найдено записей: " + list.size());
                for (Object item : list) {
                    System.out.println("  " + item);
                }
            } else {
                Object item = Deserializer.deserialize(data, type);
                System.out.println("  " + item);
            }
        } catch (Exception e) {
            System.out.println("Данные: " + data);
        }
    }
}