package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass // Hibernate не создаёт таблицу для этого класса, но все дочерние классы наследуют его поля
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id //первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // автоинкремент, mysql сам делает айди = прошлый + 1
    protected Long id;

    @Column(name = "created_at") // поле createdAt в классе соответствует колонке created_at в таблице
    protected LocalDateTime createdAt;

    // метод, помеченный этой аннотацией, выполнится автоматически ровно за мгновение до того, как Hibernate отправит команду INSERT в базу данных
    @PrePersist // сделать ПЕРЕД сохранением.
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // текущее время
        }
    }
}