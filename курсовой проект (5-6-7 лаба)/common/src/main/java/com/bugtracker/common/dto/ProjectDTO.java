package com.bugtracker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;       // ID владельца, а не весь объект User
    private String ownerName;   // имя владельца (для отображения в таблице)
    private int membersCount;   // количество участников (удобно для списка)
}