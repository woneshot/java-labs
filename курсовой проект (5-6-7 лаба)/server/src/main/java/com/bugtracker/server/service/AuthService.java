package com.bugtracker.server.service;

import com.bugtracker.common.entity.User;
import com.bugtracker.server.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository = new UserRepository(); // сервис использует репозиторий

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        if (!user.getIsActive()) {
            return Optional.empty();
        }

        String hashedPassword = hashPassword(password);
        System.out.println("Введённый пароль хеш: " + hashedPassword);
        System.out.println("Пароль из БД:         " + user.getPassword());

        if (!user.getPassword().equals(hashedPassword)) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ошибка хэширования пароля", e);
        }
    }
}