package com.server.services;

import com.server.repositories.DeveloperDAO;
import com.server.repositories.DeveloperGameDAO;
import com.server.repositories.GameDAO;
import com.server.exceptions.ResponseException;
import com.server.model.entities.Developer;
import com.server.model.entities.Game;

import java.util.ArrayList;
import java.util.List;

public class DeveloperGameService {
    private final DeveloperGameDAO linkDAO;
    private final GameDAO gameDAO;
    private final DeveloperDAO developerDAO;

    public DeveloperGameService() {
        this.linkDAO = new DeveloperGameDAO();
        this.gameDAO = new GameDAO();
        this.developerDAO = new DeveloperDAO();
    }

    public void addLink(int gameId, int developerId) {
        Game game = gameDAO.findById(gameId);
        if (game == null) {
            throw new ResponseException("Game not found");
        }
        Developer developer = developerDAO.findById(developerId);
        if (developer == null) {
            throw new ResponseException("Developer not found");
        }
        linkDAO.addLink(gameId, developerId);
    }

    public void removeLink(int gameId, int developerId) {
        linkDAO.removeLink(gameId, developerId);
    }

    public List<Developer> getDevelopersByGame(int gameId) {
        List<Integer> developerIds = linkDAO.getDeveloperIdsByGame(gameId);
        List<Developer> developers = new ArrayList<>();
        for (int developerId : developerIds) {
            Developer developer = developerDAO.findById(developerId);
            if (developer != null) {
                developers.add(developer);
            }
        }
        return developers;
    }

    public List<Game> getGamesByDeveloper(int developerId) {
        List<Integer> gameIds = linkDAO.getGameIdsByDeveloper(developerId);
        List<Game> games = new ArrayList<>();
        for (int gameId : gameIds) {
            Game game = gameDAO.findById(gameId);
            if (game != null) {
                games.add(game);
            }
        }
        return games;
    }
}