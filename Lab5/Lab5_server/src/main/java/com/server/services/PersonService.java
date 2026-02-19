package com.server.services;

import com.server.exceptions.ResponseException;
import com.server.interfaces.Service;
import com.server.model.entities.Person;
import com.server.repositories.PersonDAO;

import java.util.List;

public class PersonService implements Service<Person> {

    private final PersonDAO personDAO;

    public PersonService() {
        personDAO = new PersonDAO();
    }

    @Override
    public void create(Person entity) {
        if (entity == null) {
            throw new ResponseException("Person is null");
        }
        personDAO.create(entity);
    }

    @Override
    public Person getById(int id) {
        Person person = personDAO.findById(id);
        if (person == null) {
            throw new ResponseException("Person not found");
        }
        return person;
    }

    @Override
    public List<Person> getAll() {
        return personDAO.findAll();
    }

    @Override
    public void update(Person entity) {
        if (entity == null) {
            throw new ResponseException("Person is null");
        }
        personDAO.update(entity);
    }

    @Override
    public void delete(int id) {
        Person person = personDAO.findById(id);
        if (person == null) {
            throw new ResponseException("Person not found");
        }
        personDAO.delete(id);
    }
}