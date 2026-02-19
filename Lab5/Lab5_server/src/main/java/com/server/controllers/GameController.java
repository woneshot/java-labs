package com.server.controllers;
import com.server.model.entities.Game;
import com.server.network.Request;
import com.server.network.Response;
import com.server.serializer.Deserializer;
import com.server.serializer.Serializer;
import com.server.services.DeveloperGameService;
import com.server.services.GameService;

import java.util.List;

public class GameController {
    private static final GameService gameService = new GameService();
    private static final DeveloperGameService developerGameService = new DeveloperGameService();
    public Response createGame(Request request) {
        gameService.create(Deserializer.deserialize(request.getData(), Game.class));
        return new Response(true, "Game created successfully!", null);

    }
    public Response getAllGames() {
        return new Response(true, "Games: ", Serializer.serialize(gameService.getAll()));
    }
    public Response getGameByTitle(Request request) {
        return new Response(true, "Game: ", Serializer.serialize(gameService.getById(Integer.parseInt(request.getData()))));
    }
    public Response updateGame(Request request) {
        gameService.update(Deserializer.deserialize(request.getData(), Game.class));
        return new Response(true, "Game updated successfully!", null);
    }
    public Response deleteGame(Request request) {
        gameService.delete(Integer.parseInt(request.getData()));
        return new Response(true, "Game deleted successfully!", null);
    }
    public Response getDevelopers(Request request) {
        return new Response(true, "Developers: ", Serializer.serialize(developerGameService.getDevelopersByGame(Integer.parseInt(request.getData()))));
    }
}