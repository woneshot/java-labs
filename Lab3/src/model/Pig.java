package model;

public class Pig extends Animal {
    private final int fatPercentage;

    public Pig(String name, String type, String gender, int weight, int age, int id, int fatPercentage) {
        super(name, type, gender, weight, age, id);
        this.fatPercentage = fatPercentage;
    }

    public int getFatPercentage() {
        return fatPercentage;
    }

    @Override
    public String getProductType() {
        return "Сало";
    }

    @Override
    public double getProductAmount() {
        return fatPercentage;
    }

    @Override
    public String getFeedType() {
        return "Комбикорм";
    }

    @Override
    public void feed() {
        System.out.println("Свинья " + getName() + " ест комбикорм.");
    }

    @Override
    public String toString() {
        return super.toString() + " | Жирность: " + fatPercentage + "%";
    }
}