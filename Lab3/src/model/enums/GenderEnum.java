package model.enums;

public enum GenderEnum {
    MALE("Самец"),
    FEMALE("Самка");

    private final String label;

    GenderEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}