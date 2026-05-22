package com.bugtracker.server.service;

import com.bugtracker.common.dto.UserDTO;
import com.bugtracker.common.entity.Role;
import com.bugtracker.common.entity.User;
import com.bugtracker.server.repository.RoleRepository;
import com.bugtracker.server.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final RoleRepository roleRepository = new RoleRepository();
    private final AuthService authService = new AuthService(); // для функции хеширования

    public User createUser(String username, String password, String email, String fullName, String roleName) {
        // проверяем уникальность
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        // ищем роль
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName));

        // создаём пользователя
        User user = new User();
        user.setUsername(username);
        user.setPassword(authService.hashPassword(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role);
        user.setIsActive(true);

        return userRepository.save(user); // сохранение бд
    }

    public User updateUser(Long id, String email, String fullName, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName));

        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role);

        return userRepository.update(user); // обновление в бд
    }

    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setIsActive(false);
        userRepository.update(user);
    }

    public void unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setIsActive(true);
        userRepository.update(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getName())
                .isActive(user.getIsActive())
                .build();
    }
}