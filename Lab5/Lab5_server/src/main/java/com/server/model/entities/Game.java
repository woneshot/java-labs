package com.server.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Serializable {
    private int id;
    private String title;
    private String genre;
    private double price;
    private int releaseYear;
    private double rating;
}