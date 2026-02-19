package model;

public class Cow extends Animal {
    private final double milkYield;

    public Cow(String name, String type, String gender, int weight, int age, int id, double milkYield) {
        super(name, type, gender, weight, age, id);
        this.milkYield = milkYield;
    }

    public double getMilkYield() {
        return milkYield;
    }

    @Override
    public String getProductType() {
        return "Молоко";
    }

    @Override
    public double getProductAmount() {
        return milkYield;
    }

    @Override
    public String getFeedType() {
        return "Сено";
    }

    @Override
    public void feed() {
        System.out.println("Корова " + getName() + " жует сено.");
    }

    @Override
    public String toString() {
        return super.toString() + " | Удой: " + milkYield + " л/день";
    }
}