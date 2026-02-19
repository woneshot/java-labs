package com.server.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Developer implements Serializable {
    private int id;
    private String name;
    private String country;
    private int foundedYear;
}