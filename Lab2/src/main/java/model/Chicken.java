package model;

public class Chicken extends Animal {
    private final int eggsPerDay;

    public Chicken(String type, String gender, int weight, int age, String name, int id, int eggsPerDay) {
        super(name, type, gender, weight, age,  id);
        this.eggsPerDay = eggsPerDay;
    }

    public int getEggsPerDay() {
        return eggsPerDay;
    }

    // Chicken
    @Override
    public String toString() {
        return super.toString() + " [Яйценоскость: " + eggsPerDay + " шт/день]";
    }
}
