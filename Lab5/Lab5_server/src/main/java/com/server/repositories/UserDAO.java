package com.server.repositories;

import com.server.interfaces.DAO;
import com.server.model.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {

    private static List<User> storage = new ArrayList<>();

    @Override
    public void create(User entity) {
        storage.add(entity);
    }

    @Override
    public User findById(int id) {
        return storage.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User findByLogin(String login) {
        return storage.stream()
                .filter(user -> user.getUsername().equals(login))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void update(User entity) {
        int index = storage.indexOf(findById(entity.getId()));
        storage.set(index, entity);
    }

    @Override
    public void delete(int id) {
        storage.remove(findById(id));
    }
}