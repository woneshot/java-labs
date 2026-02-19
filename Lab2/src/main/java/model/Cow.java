package model;

public class Cow extends Animal {
    private final double milkYield;

    public Cow(String type, String gender, int weight, int age, String name, int id, double milkYield) {
        super(name, type, gender, weight, age,  id);
        this.milkYield = milkYield;
    }

    public double getMilkYield() {
        return milkYield;
    }

    @Override
    public String toString() {
        return super.toString() + " [Удой: " + milkYield + " л/день]";
    }
}
