package com.server.network;

import com.server.controllers.*;
import com.server.enums.Operation;
import com.server.exceptions.ResponseException;
import com.server.model.entities.Game;
import com.server.model.entities.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread {
    private final Socket clientSocket;
    private final GameController gameController;
    private final UserController userController;
    private final DeveloperController developerController;
    private final RoleController roleController;
    private final DeveloperGameController developerGameController;
    private User currentUser = null;

    public ClientThread(Socket socket) {
        this.clientSocket = socket;
        gameController = new GameController();
        userController = new UserController();
        developerController = new DeveloperController();
        roleController = new RoleController();
        developerGameController = new DeveloperGameController();
    }

    public void run() {
        try (
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            boolean keepRunning = true;
            while (keepRunning) {
                try {
                    Request request = (Request) input.readObject();

                    if (request != null) {
                        Response response = processRequest(request);
                        if (request.getOperation() == Operation.DISCONNECT) {
                            keepRunning = false;
                        }
                        output.writeObject(response);
                        output.flush();
                    } else {
                        Response errorResponse = new Response(false, "Received invalid object", null);
                        output.writeObject(errorResponse);
                        output.flush();
                    }
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                    keepRunning = false;
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + e.getMessage());
                    keepRunning = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    keepRunning = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    private Response processRequest(Request request) {
        try {
            return switch (request.getOperation()) {
                case LOGIN -> {
                    Response resp = userController.login(request);
                    if (resp.isSuccess() && resp.getData() != null) {
                        currentUser = new com.google.gson.Gson().fromJson(resp.getData(), User.class);
                    }
                    yield resp;
                }
                case REGISTER -> {
                    Response resp = userController.register(request);
                    if (resp.isSuccess() && resp.getData() != null) {
                        currentUser = new com.google.gson.Gson().fromJson(resp.getData(), User.class);
                    }
                    yield resp;
                }

                case GET_ALL_GAMES -> requireAuth(() -> gameController.getAllGames());
                case READ_GAME_DATA -> requireAuth(() -> gameController.getGameByTitle(request));
                case GET_ALL_DEVELOPERS -> requireAuth(() -> developerController.getAllDevelopers());
                case GET_DEVELOPERS_BY_GAME -> requireAuth(() -> gameController.getDevelopers(request));
                case GET_ALL_ROLES -> requireAuth(() -> roleController.getAllRoles());

                case CREATE_GAME -> requireRole(2, () -> {
                    Response resp = gameController.createGame(request);
                    if (resp.isSuccess() && currentUser.getRoleId() == 2) {
                        developerGameController.autoLink(currentUser.getId(), request);
                    }
                    return resp;
                });

                case UPDATE_GAME -> requireOwnerOrAdmin(request, () -> gameController.updateGame(request));
                case DELETE_GAME -> requireOwnerOrAdmin(request, () -> gameController.deleteGame(request));

                case GET_ALL_USERS -> requireRole(3, () -> userController.getAllUsers());
                case READ_USER -> requireRole(3, () -> userController.readEntity(request));
                case DELETE_USER -> requireRole(3, () -> userController.deleteUser(request));
                case UPDATE_USER -> requireRole(3, () -> userController.updateEntity(request));
                case JOIN_DEVELOPER_GAME -> requireRole(3, () ->
                        developerGameController.processDeveloperGameRelationship(request));
                case SEPARATE_DEVELOPER_GAME -> requireRole(3, () ->
                        developerGameController.processDeveloperGameRelationship(request));

                case DISCONNECT -> new Response(true, "Disconnected successfully", null);
                default -> new Response(false, "Received unknown operation", null);
            };
        } catch (ResponseException e) {
            return new Response(false, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Something went wrong on the server side!", null);
        }
    }

    private Response requireAuth(ResponseSupplier supplier) {
        if (currentUser == null) {
            return new Response(false, "Необходимо войти в систему", null);
        }
        return supplier.get();
    }

    private Response requireRole(int minRoleId, ResponseSupplier supplier) {
        if (currentUser == null) {
            return new Response(false, "Необходимо войти в систему", null);
        }
        if (currentUser.getRoleId() < minRoleId) {
            return new Response(false, "Недостаточно прав", null);
        }
        return supplier.get();
    }

    private Response requireOwnerOrAdmin(Request request, ResponseSupplier supplier) {
        if (currentUser == null) {
            return new Response(false, "Необходимо войти в систему", null);
        }
        if (currentUser.getRoleId() == 3) {
            return supplier.get();
        }
        if (currentUser.getRoleId() == 2) {
            int gameId = extractGameId(request);
            if (developerGameController.isDeveloperLinked(currentUser.getId(), gameId)) {
                return supplier.get();
            }
            return new Response(false, "Это не ваша игра", null);
        }
        return new Response(false, "Недостаточно прав", null);
    }

    private int extractGameId(Request request) {
        try {
            return Integer.parseInt(request.getData());
        } catch (NumberFormatException e) {
            Game game = new com.google.gson.Gson().fromJson(request.getData(), Game.class);
            return game.getId();
        }
    }

    @FunctionalInterface
    private interface ResponseSupplier {
        Response get();
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}