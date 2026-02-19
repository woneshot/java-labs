package model;

public class Pig extends Animal {
    private final int fatPercentage;

    public Pig(String type, String gender, int weight, int age, String name, int id, int fatPercentage) {
        super(name, type, gender, weight, age,  id);
        this.fatPercentage = fatPercentage;
    }

    public int getFatPercentage() {
        return fatPercentage;
    }

    // Pig
    @Override
    public String toString() {
        return super.toString() + " [Жирность: " + fatPercentage + "%]";
    }
}
