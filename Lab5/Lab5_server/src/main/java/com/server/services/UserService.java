package com.server.services;

import com.server.exceptions.ResponseException;
import com.server.interfaces.Service;
import com.server.model.entities.Developer;
import com.server.model.entities.User;
import com.server.repositories.DeveloperDAO;
import com.server.repositories.UserDAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService implements Service<User> {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    @Override
    public void create(User entity) {
        if (entity == null) {
            throw new ResponseException("User is null");
        }
        userDAO.create(entity);
    }

    @Override
    public User getById(int id) {
        User user = userDAO.findById(id);
        if (user == null) {
            throw new ResponseException("User not found");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return userDAO.findAll();
    }

    @Override
    public void update(User entity) {
        if (entity == null) {
            throw new ResponseException("User is null");
        }
        userDAO.update(entity);
    }

    @Override
    public void delete(int id) {
        User user = userDAO.findById(id);
        if (user == null) {
            throw new ResponseException("User not found");
        }
        userDAO.delete(id);
    }

    public User login(String username, String password) {
        User user = userDAO.findByLogin(username);
        if (user == null) {
            throw new ResponseException("User not found");
        }

        String hashedPassword;
        try {
            hashedPassword = java.util.Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(password.getBytes())
            );
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseException("Error while hashing password");
        }

        if (!user.getPassword().equals(hashedPassword)) {
            throw new ResponseException("Invalid password");
        }
        return user;
    }

    public User register(String username, String password, int roleId) {
        if (roleId == 3) {
            throw new ResponseException("Cannot register as admin");
        }
        if (userDAO.findByLogin(username) != null) {
            throw new ResponseException("User already exists");
        }
        int id = getAll().size() + 1;
        String hashedPassword;
        try {
            hashedPassword = java.util.Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(password.getBytes())
            );
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseException("Error while hashing password");
        }
        User user = new User(id, username, hashedPassword, roleId);
        userDAO.create(user);

        if (roleId == 2) {
            new DeveloperDAO().create(new Developer(id, username, "Unknown", 0));
        }

        return user;
    }
}