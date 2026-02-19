package com.server.controllers;

import com.server.enums.Operation;
import com.server.model.entities.Game;
import com.server.network.Request;
import com.server.network.Response;
import com.server.services.DeveloperGameService;

public class DeveloperGameController {
    private static final DeveloperGameService developerGameService = new DeveloperGameService();

    public Response processDeveloperGameRelationship(Request request) {
        int gameId = Integer.parseInt(request.getData().split(":")[0]);
        int developerId = Integer.parseInt(request.getData().split(":")[1]);

        if (request.getOperation() == Operation.JOIN_DEVELOPER_GAME) {
            developerGameService.addLink(gameId, developerId);
        } else if (request.getOperation() == Operation.SEPARATE_DEVELOPER_GAME) {
            developerGameService.removeLink(gameId, developerId);
        }

        return new Response(true, "Success", null);
    }

    public void autoLink(int developerId, Request request) {
        Game game = new com.google.gson.Gson().fromJson(request.getData(), Game.class);
        developerGameService.addLink(game.getId(), developerId);
    }

    public boolean isDeveloperLinked(int developerId, int gameId) {
        return developerGameService.getDevelopersByGame(gameId)
                .stream()
                .anyMatch(dev -> dev.getId() == developerId);
    }
}