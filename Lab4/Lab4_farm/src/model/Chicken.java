package model;

public class Chicken extends Animal {
    private final int eggsPerDay;

    public Chicken(String name, String type, String gender, int weight, int age, int id, int eggsPerDay) {
        super(name, type, gender, weight, age, id);
        this.eggsPerDay = eggsPerDay;
    }

    public int getEggsPerDay() {
        return eggsPerDay;
    }

    @Override
    public String getProductType() {
        return "Яйца";
    }

    @Override
    public double getProductAmount() {
        return eggsPerDay;
    }

    @Override
    public String getFeedType() {
        return "Зерно";
    }

    @Override
    public void feed() {
        System.out.println("Курица " + getName() + " клюет зерно.");
    }

    @Override
    public String toString() {
        return super.toString() + " | Яйценоскость: " + eggsPerDay + " шт/день";
    }
}