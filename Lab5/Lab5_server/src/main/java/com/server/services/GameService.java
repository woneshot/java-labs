package com.server.services;

import com.server.exceptions.ResponseException;
import com.server.interfaces.Service;
import com.server.model.entities.Game;
import com.server.repositories.GameDAO;

import java.util.List;

public class GameService implements Service<Game> {
    private final GameDAO gameDAO;

    public GameService() {
        gameDAO = new GameDAO();
    }

    @Override
    public void create(Game entity) {
        if (entity == null) {
            throw new ResponseException("Game is null");
        }
        gameDAO.create(entity);
    }

    @Override
    public Game getById(int id) {
        Game game = gameDAO.findById(id);
        if (game == null) {
            throw new ResponseException("Game not found");
        }
        return game;
    }

    @Override
    public List<Game> getAll() {
        return gameDAO.findAll();
    }

    @Override
    public void update(Game entity) {
        if (entity == null) {
            throw new ResponseException("Game is null");
        }
        gameDAO.update(entity);
    }

    @Override
    public void delete(int id) {
        Game game = gameDAO.findById(id);
        if (game == null) {
            throw new ResponseException("Game not found");
        }
        gameDAO.delete(id);
    }
}