package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    // много пользователей могут иметь одну роль
    @ManyToOne(fetch = FetchType.EAGER) // жадная загрузка: когда мы загружаем пользователя из БД, Hibernate сразу же подгружает и его роль
    @JoinColumn(name = "role_id", nullable = false) // внешний ключ
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive = true; // вместо удаления будем деактивировать (чтобы не ломать данные и не делать мертвые ссылки)
}