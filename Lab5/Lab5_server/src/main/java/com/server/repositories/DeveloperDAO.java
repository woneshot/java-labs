package com.server.repositories;

import com.server.interfaces.DAO;
import com.server.model.entities.Developer;

import java.util.ArrayList;
import java.util.List;

public class DeveloperDAO implements DAO<Developer> {
    private static List<Developer> storage = new ArrayList<>();

    @Override
    public void create(Developer entity) {
        storage.add(entity);
    }

    @Override
    public Developer findById(int id) {
        return storage.stream()
                .filter(developer -> developer.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Developer> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void update(Developer entity) {
        int index = storage.indexOf(findById(entity.getId()));
        storage.set(index, entity);
    }

    @Override
    public void delete(int id) {
        storage.remove(findById(id));
    }
}