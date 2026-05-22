package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT") // длинная строка ~65к символов
    private String description;

    @ManyToOne(fetch = FetchType.EAGER) // сразу подгружаем создателя проекта
    @JoinColumn(name = "owner_id", nullable = false) // внешний ключ
    private User owner;
}