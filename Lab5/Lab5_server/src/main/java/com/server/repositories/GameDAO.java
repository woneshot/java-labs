package com.server.repositories;

import com.server.exceptions.ResponseException;
import com.server.interfaces.DAO;
import com.server.model.entities.Game;

import java.util.ArrayList;
import java.util.List;


public class GameDAO implements DAO<Game> {
    private static List<Game> storage = new ArrayList<>();

    @Override
    public void create(Game entity) {
        storage.add(entity);
    }

    @Override
    public Game findById(int id) {
        return storage.stream()
                .filter(game -> game.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void update(Game entity) {
        int index = storage.indexOf(findById(entity.getId()));
        if (index == -1) {
            throw new ResponseException("Game not found");
        }
        storage.set(index, entity);
    }

    @Override
    public void delete(int id) {
        storage.remove(findById(id));
    }
}