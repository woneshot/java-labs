package com.server.repositories;

import com.server.interfaces.DAO;
import com.server.model.entities.Role;

import java.util.ArrayList;
import java.util.List;


public class RoleDAO implements DAO<Role> {
    private static List<Role> storage = new ArrayList<>();

    @Override
    public void create(Role entity) {
        storage.add(entity);
    }

    @Override
    public Role findById(int id) {
        return storage.stream()
                .filter(role -> role.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void update(Role entity) {
        int index = storage.indexOf(findById(entity.getId()));
        storage.set(index, entity);
    }

    @Override
    public void delete(int id) {
        storage.remove(findById(id));
    }
}