package model;

import java.io.Serializable;

public class Vaccination implements Serializable {
    private final String animalName;
    private final String vaccineName;
    private final String date;

    public Vaccination(String animalName, String vaccineName, String date) {
        this.animalName = animalName;
        this.vaccineName = vaccineName;
        this.date = date;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + animalName + " — вакцина: " + vaccineName;
    }
}