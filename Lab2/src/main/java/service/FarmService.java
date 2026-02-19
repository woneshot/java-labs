package service;

import model.Animal;
import model.Chicken;
import model.Cow;
import model.Pig;
import model.enums.TypeEnum;

import java.io.*;
import java.util.*;

public class FarmService {
    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    // Проверка уникальности имени
    public boolean isNameTaken(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // Проверка уникальности ID
    public boolean isIdTaken(int id) {
        for (Animal animal : animals) {
            if (animal.getId() == id) {
                return true;

            }
        }
        return false;
    }

    public void deleteAnimalByName(String name) {
        Animal animalToDelete = null;

        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                animalToDelete = animal;
                break;
            }
        }

        if (animalToDelete != null) {
            animals.remove(animalToDelete);
            System.out.println("Животное " + name + " успешно удалено.");
        } else {
            System.out.println("Животное с именем " + name + " не найдено.");
        }
    }

    // Принимаем Scanner из Main
    public void editAgeByName(String name, Scanner sc) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                System.out.print("Текущий возраст " + animal.getAge() + ". Введите новый: ");
                try {
                    int age = Integer.parseInt(sc.nextLine());
                    animal.setAge(age);
                    System.out.println("Возраст успешно обновлен.");
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: нужно ввести число!");
                }
                return;
            }
        }
        System.out.println("Животное с именем " + name + " не найдено.");
    }

    // Принимаем Scanner из Main
    public void editWeightByName(String name, Scanner sc) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                System.out.print("Текущий вес " + animal.getWeight() + ". Введите новый: ");
                try {
                    int weight = Integer.parseInt(sc.nextLine());
                    animal.setWeight(weight);
                    System.out.println("Вес успешно обновлен.");
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: нужно ввести число!");
                }
                return;
            }
        }
        System.out.println("Животное с именем " + name + " не найдено.");
    }

    public void searchByName(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                System.out.println("Животное с кличкой " + name + " найдено!");
                System.out.println(animal.toString());
                return;
            }
        }
        System.out.println("Животное с кличкой " + name + " не найдено!");
    }

    public void filterByType(String type) {
        try {
            TypeEnum filterType = TypeEnum.valueOf(type.toUpperCase().trim());
            boolean found = false;
            for (Animal animal : animals) {
                if (animal.getType() == filterType) {
                    System.out.println(animal);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("На ферме нет таких животных!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный тип! Доступны: COW, CHICKEN, PIG.");
        }
    }

    public void filterByAge(int age) {
        boolean found = false;
        for (Animal animal : animals) {
            if (animal.getAge() == age) {
                System.out.println(animal);
                found = true;
            }
        }
        if (!found) {
            System.out.println("На ферме нет животных с указанным возрастом!");
        }
    }

    public void filterByWeight(int weight) {
        boolean found = false;
        for (Animal animal : animals) {
            if (animal.getWeight() == weight) {
                System.out.println(animal);
                found = true;
            }
        }
        if (!found) {
            System.out.println("На ферме нет животных с указанным весом!");
        }
    }

    public void sortByName() {
        animals.sort(Comparator.comparing(Animal::getName));
    }

    public void sortByWeight() {
        animals.sort(Comparator.comparingInt(Animal::getWeight));
    }

    public void statsByType(String input) {
        TypeEnum type;
        try {
            type = TypeEnum.valueOf(input.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Тип животного '" + input + "' не существует. Доступные типы: COW, CHICKEN, PIG.");
            return;
        }

        int count = 0;
        int totalWeight = 0;
        int totalAge = 0;

        double totalMilk = 0;
        int totalEggs = 0;
        int totalFat = 0;

        for (Animal animal : animals) {
            if (animal.getType() == type) {
                count++;
                totalWeight += animal.getWeight();
                totalAge += animal.getAge();

                if (animal instanceof Cow) {
                    totalMilk += ((Cow) animal).getMilkYield();
                } else if (animal instanceof Chicken) {
                    totalEggs += ((Chicken) animal).getEggsPerDay();
                } else if (animal instanceof Pig) {
                    totalFat += ((Pig) animal).getFatPercentage();
                }
            }
        }

        if (count == 0) {
            System.out.println("Животных данного типа в списке нет.");
            return;
        }

        System.out.println("Количество голов: " + count);
        System.out.println("Средний вес: " + ((double) totalWeight / count) + " кг");
        System.out.println("Средний возраст: " + ((double) totalAge / count) + " лет");

        switch (type) {
            case COW:
                System.out.println("Общий удой: " + totalMilk + " л.");
                System.out.println("Средний удой: " + (totalMilk / count) + " л.");
                break;
            case CHICKEN:
                System.out.println("Всего яиц в день: " + totalEggs + " шт.");
                System.out.println("Средняя яйценоскость: " + ((double) totalEggs / count) + " шт.");
                break;
            case PIG:
                System.out.println("Средний процент жира: " + ((double) totalFat / count) + "%");
                break;
        }
    }

    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Animal animal : animals) {
                String line = animal.getType() + ";" + animal.getName() + ";" + animal.getGender() + ";"
                        + animal.getWeight() + ";" + animal.getAge() + ";" + animal.getId() + ";";

                if (animal instanceof Cow cow) line += cow.getMilkYield();
                else if (animal instanceof Chicken chicken) line += chicken.getEggsPerDay();
                else if (animal instanceof Pig pig) line += pig.getFatPercentage();

                writer.write(line + "\n");
            }
            System.out.println("Успешно сохранено!");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        animals.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 7) continue;

                String type = parts[0];
                String name = parts[1];
                String gender = parts[2];
                int weight = Integer.parseInt(parts[3]);
                int age = Integer.parseInt(parts[4]);
                int id = Integer.parseInt(parts[5]);

                switch (type) {
                    case "COW" -> animals.add(
                            new Cow(type, gender, weight, age, name, id, Double.parseDouble(parts[6]))
                    );
                    case "CHICKEN" -> animals.add(
                            new Chicken(type, gender, weight, age, name, id, Integer.parseInt(parts[6]))
                    );
                    case "PIG" -> animals.add(
                            new Pig(type, gender, weight, age, name, id, Integer.parseInt(parts[6]))
                    );
                }
            }
            System.out.println("Успешно загружено!");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата данных в файле.");
        }
    }

    public void printAllAnimals() {
        if (animals.isEmpty()) {
            System.out.println("Список животных пуст.");
            return;
        }
        for (Animal animal : animals) {
            System.out.println(animal.toString());
        }
    }


}