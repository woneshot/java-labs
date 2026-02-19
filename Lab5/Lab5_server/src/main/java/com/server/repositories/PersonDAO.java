package com.server.repositories;

import com.server.interfaces.DAO;
import com.server.model.entities.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the DAO interface for the T entity.
 * It provides methods for creating, finding, updating and deleting Person objects.
 */
public class PersonDAO implements DAO<Person> {
    private static List<Person> storage = new ArrayList<>();

    @Override
    public void create(Person entity) {
        storage.add(entity);
    }

    @Override
    public Person findById(int id) {
        return storage.stream()
                .filter(person -> person.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void update(Person entity) {
        int index = storage.indexOf(findById(entity.getId()));
        storage.set(index, entity);
    }

    @Override
    public void delete(int id) {
        storage.remove(findById(id));
    }
}
