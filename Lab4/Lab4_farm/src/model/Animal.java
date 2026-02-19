package model;

import model.enums.GenderEnum;
import model.enums.TypeEnum;
import interfaces.Feedable;
import interfaces.Productive;
import java.io.Serializable;

public abstract class Animal implements Feedable, Productive, Serializable {
    private final int id;
    private final String name;
    private int age;
    private int weight;
    private final GenderEnum gender;
    private final TypeEnum type;

    public Animal(String name, String type, String gender, int weight, int age, int id) {
        this.type = TypeEnum.valueOf(type.toUpperCase().trim());
        this.gender = GenderEnum.valueOf(gender.toUpperCase().trim());
        this.weight = weight;
        this.age = age;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = Math.max(1, age);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = Math.max(1, weight);
    }

    public GenderEnum getGender() {
        return gender;
    }

    public TypeEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " #" + id + " | " + name + " (" + gender + ") | Вес: " + weight + " кг | Возраст: " + age + " лет";
    }
}