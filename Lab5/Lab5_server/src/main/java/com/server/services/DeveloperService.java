package com.server.services;

import com.server.exceptions.ResponseException;
import com.server.interfaces.Service;
import com.server.model.entities.Developer;
import com.server.repositories.DeveloperDAO;

import java.util.List;

public class DeveloperService implements Service<Developer> {
    private final DeveloperDAO developerDAO;

    public DeveloperService() {
        developerDAO = new DeveloperDAO();
    }


    @Override
    public void create(Developer entity) {
        if (entity == null) {
            throw new ResponseException("Developer is null");
        }
        developerDAO.create(entity);
    }

    @Override
    public Developer getById(int id) {
        Developer developer = developerDAO.findById(id);
        if (developer == null) {
            throw new ResponseException("Developer not found");
        }
        return developer;
    }

    @Override
    public List<Developer> getAll() {
        return developerDAO.findAll();
    }

    @Override
    public void update(Developer entity) {
        if (entity == null) {
            throw new ResponseException("Developer is null");
        }
        developerDAO.update(entity);
    }

    @Override
    public void delete(int id) {
        Developer developer = developerDAO.findById(id);
        if (developer == null) {
            throw new ResponseException("Developer not found");
        }
        developerDAO.delete(id);
    }
}
