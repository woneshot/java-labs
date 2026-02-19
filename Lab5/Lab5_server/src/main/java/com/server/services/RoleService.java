package com.server.services;

import com.server.exceptions.ResponseException;
import com.server.interfaces.Service;
import com.server.model.entities.Role;
import com.server.repositories.RoleDAO;

import java.util.List;

public class RoleService implements Service<Role> {
    private final RoleDAO roleDAO;

    public RoleService() {
        roleDAO = new RoleDAO();
    }

    @Override
    public void create(Role entity) {
        if (entity == null) {
            throw new ResponseException("Role is null");
        }
        roleDAO.create(entity);
    }

    @Override
    public Role getById(int id) {
        Role role = roleDAO.findById(id);
        if (role == null) {
            throw new ResponseException("Role not found");
        }
        return role;
    }

    @Override
    public List<Role> getAll() {
        return roleDAO.findAll();
    }

    @Override
    public void update(Role entity) {
        if (entity == null) {
            throw new ResponseException("Role is null");
        }
        roleDAO.update(entity);
    }

    @Override
    public void delete(int id) {
        Role role = roleDAO.findById(id);
        if (role == null) {
            throw new ResponseException("Role not found");
        }
        roleDAO.delete(id);
    }
}