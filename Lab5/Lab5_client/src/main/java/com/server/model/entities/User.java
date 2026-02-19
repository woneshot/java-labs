package com.server.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private int roleId;

    @Override
    public String toString() {
        return "| ID: " + id + " | " + username + " | Роль: " +
                (roleId == 3 ? "ADMIN" : roleId == 2 ? "DEVELOPER" : "USER") + " |";
    }
}