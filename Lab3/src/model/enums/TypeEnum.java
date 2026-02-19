package model.enums;

public enum TypeEnum {
    COW("Корова"),
    CHICKEN("Курица"),
    PIG("Свинья");

    private final String label;

    TypeEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}