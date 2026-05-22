package com.bugtracker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // типо стрингбилдер
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;     // строка ("ADMIN"), а не объект Role
    private Boolean isActive;
    // в отличие от сущности юзера здесь нет пароля
}