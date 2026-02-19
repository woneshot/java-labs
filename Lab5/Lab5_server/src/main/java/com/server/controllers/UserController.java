package com.server.controllers;

import com.server.model.entities.User;
import com.server.network.Request;
import com.server.network.Response;
import com.server.serializer.Deserializer;
import com.server.serializer.Serializer;
import com.server.services.UserService;

public class UserController {
    private static final UserService userService = new UserService();

    public Response login(Request request) {
        String[] data = request.getData().split(":");
        return new Response(true, "User logged in successfully!",
                Serializer.serialize(userService.login(data[0], data[1])));
    }

    public Response register(Request request) {
        String[] data = request.getData().split(":");
        return new Response(true, "User registered successfully!",
                Serializer.serialize(userService.register(data[0], data[1], Integer.parseInt(data[2]))));
    }

    public Response getAllUsers() {
        return new Response(true, "Users: ", Serializer.serialize(userService.getAll()));
    }

    public Response deleteUser(Request request) {
        userService.delete(Integer.parseInt(request.getData()));
        return new Response(true, "User deleted successfully!", null);
    }

    public Response updateEntity(Request request) {
        userService.update(Deserializer.deserialize(request.getData(), User.class));
        return new Response(true, "User updated successfully!", null);
    }

    public Response readEntity(Request request) {
        return new Response(true, "User: ",
                Serializer.serialize(userService.getById(Integer.parseInt(request.getData()))));
    }
}