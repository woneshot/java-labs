package com.bugtracker.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // этот объект - строка в таблице базы данных
@Table(name = "roles") // соответствует таблице roles
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 255)
    private String description;
}