package service;

import model.*;
import model.enums.TypeEnum;
import model.enums.GenderEnum;
import interfaces.Productive;
import utils.Journal;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FarmService {
    private List<Animal> animals = new ArrayList<>();

//crud & checks
    private void addAnimal(Animal animal) {
        animals.add(animal);
        Journal.log("Добавлено животное: " + animal.getName() + " (" + animal.getType() + ")");
    }

    private boolean isNameTaken(String name) {
        for (Animal a : animals) {
            if (a.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIdTaken(int id) {
        for (Animal a : animals) {
            if (a.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnimal(String name) {
        for (Animal a : animals) {
            if (a.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void createNewAnimal(Scanner sc) {
        System.out.println("\n--- СОЗДАНИЕ НОВОГО ЖИВОТНОГО ---");

        System.out.print("Тип (COW, CHICKEN, PIG): ");
        String typeStr = sc.nextLine().toUpperCase().trim();

        if (!typeStr.equals("COW") && !typeStr.equals("CHICKEN") && !typeStr.equals("PIG")) {
            System.out.println("Ошибка: Неверный тип животного. Доступны только COW, CHICKEN, PIG.");
            return;
        }

        System.out.print("Имя: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ошибка: Имя не может быть пустым.");
            return;
        }
        if (isNameTaken(name)) {
            System.out.println("Ошибка: Животное с именем \"" + name + "\" уже существует.");
            return;
        }

        System.out.print("Пол (MALE, FEMALE): ");
        String genderStr = sc.nextLine().toUpperCase().trim();
        try {
            GenderEnum.valueOf(genderStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Неверный пол. Доступны только MALE, FEMALE.");
            return;
        }

        int weight, age, id;

        try {
            System.out.print("Вес (кг): ");
            weight = Integer.parseInt(sc.nextLine());

            System.out.print("Возраст (лет): ");
            age = Integer.parseInt(sc.nextLine());

            System.out.print("ID: ");
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введено не число! Операция отменена.");
            return;
        }

        if (isIdTaken(id)) {
            System.out.println("Ошибка: Животное с ID=" + id + " уже существует.");
            return;
        }

        try {
            if (typeStr.equals("COW")) {
                System.out.print("Удой молока (литров): ");
                double milk = Double.parseDouble(sc.nextLine());
                addAnimal(new Cow(name, typeStr, genderStr, weight, age, id, milk));
                System.out.println("Корова добавлена.");
            } else if (typeStr.equals("CHICKEN")) {
                System.out.print("Яиц в день (шт): ");
                int eggs = Integer.parseInt(sc.nextLine());
                addAnimal(new Chicken(name, typeStr, genderStr, weight, age, id, eggs));
                System.out.println("Курица добавлена.");
            } else {
                System.out.print("Процент жирности (%): ");
                int fat = Integer.parseInt(sc.nextLine());
                addAnimal(new Pig(name, typeStr, genderStr, weight, age, id, fat));
                System.out.println("Свинья добавлена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введено не число! Операция отменена.");
        }
    }

    public void deleteAnimalByName(String name) {
        Animal toRemove = null;
        for (Animal a : animals) {
            if (a.getName().equalsIgnoreCase(name)) {
                toRemove = a;
                break;
            }
        }

        if (toRemove != null) {
            animals.remove(toRemove);
            System.out.println("Животное " + name + " успешно удалено.");
            Journal.log("Удалено животное: " + name);
        } else {
            System.out.println("Животное с именем " + name + " не найдено.");
        }
    }

    public void editAnimal(String name, Scanner sc) {
        Animal found = null;
        for (Animal a : animals) {
            if (a.getName().equalsIgnoreCase(name)) {
                found = a;
                break;
            }
        }

        if (found == null) {
            System.out.println("Животное не найдено.");
            return;
        }

        System.out.println("Редактируем: " + found.getName());
        System.out.println("1 - Изменить вес");
        System.out.println("2 - Изменить возраст");
        System.out.print("Ваш выбор: ");

        try {
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 1) {
                System.out.print("Введите новый вес: ");
                int w = Integer.parseInt(sc.nextLine());
                found.setWeight(w);
                Journal.log("Изменен вес у " + name + " на " + w);
            } else if (choice == 2) {
                System.out.print("Введите новый возраст: ");
                int a = Integer.parseInt(sc.nextLine());
                found.setAge(a);
                Journal.log("Изменен возраст у " + name + " на " + a);
            } else {
                System.out.println("Нет такого варианта.");
                return;
            }
            System.out.println("Данные обновлены.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: нужно число.");
        }
    }

//feed
    public void feedAll() {
        if (animals.isEmpty()) {
            System.out.println("Ферма пуста, кормить некого.");
            return;
        }
        System.out.println("\n--- КОРМЛЕНИЕ ВСЕХ ЖИВОТНЫХ ---");
        for (Animal a : animals) {
            System.out.print("[" + a.getFeedType() + "] ");
            a.feed();
        }
        System.out.println("--- Все накормлены! ---");
        Journal.log("Кормление всех животных");
    }

    public void feedByName(String name) {
        for (Animal a : animals) {
            if (a.getName().equalsIgnoreCase(name)) {
                System.out.println("Корм: " + a.getFeedType());
                a.feed();
                Journal.log("Покормлено животное: " + name);
                return;
            }
        }
        System.out.println("Животное с именем " + name + " не найдено.");
    }

//print
    public void printAll() {
        if (animals.isEmpty()) {
            System.out.println("Список животных пуст.");
            return;
        }
        System.out.println("=== ВСЕ ЖИВОТНЫЕ ===");
        for (Animal a : animals) {
            System.out.println(a);
        }
        System.out.println("====================");
    }

//search
    public void searchByName(String namePart) {
        System.out.println("Поиск по: '" + namePart + "'");

        List<Animal> results = animals.stream()
                .filter(a -> a.getName().toLowerCase().contains(namePart.toLowerCase()))
                .toList();

        if (results.isEmpty()) {
            System.out.println("Ничего не найдено.");
        } else {
            results.forEach(System.out::println);
        }
    }


//fiter
    public void filterCombined(String typeStr, int minWeight, int maxWeight, int exactAge) {
        try {
            TypeEnum type = TypeEnum.valueOf(typeStr.toUpperCase().trim());

            List<Animal> filtered = animals.stream()
                    .filter(a -> a.getType() == type)
                    .filter(a -> a.getWeight() >= minWeight && a.getWeight() <= maxWeight)
                    .filter(a -> a.getAge() == exactAge)
                    .toList();

            System.out.println("--- Результаты фильтра ---");
            if (filtered.isEmpty()) {
                System.out.println("Животных с такими параметрами нет.");
            } else {
                filtered.forEach(System.out::println);
            }

            Journal.log("Применен сложный фильтр: " + type + ", Вес:" + minWeight + "-" + maxWeight + ", Возраст:" + exactAge);

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Неверный тип животного (нужно COW, CHICKEN или PIG).");
        }
    }

//sorts (+ comparator method)
    private Comparator<Animal> getComparator(int field) {
        if (field == 1) return Comparator.comparing(Animal::getName);
        if (field == 2) return Comparator.comparingInt(Animal::getWeight);
        if (field == 3) return Comparator.comparingInt(Animal::getAge);
        return null;
    }

    public void sortAnimals(int fieldChoice, boolean asc) {
        Comparator<Animal> comp = getComparator(fieldChoice);

        if (comp == null) {
            System.out.println("Неверный выбор поля.");
            return;
        }

        if (!asc) comp = comp.reversed();

        animals = animals.stream()
                .sorted(comp)
                .collect(Collectors.toList()); //коллекторс чтобы список остался изменяемым

        System.out.println("Список успешно отсортирован.");
        printAll();
        Journal.log("Сортировка (поле " + fieldChoice + ", ASC: " + asc + ")");
    }

    public void sortByTwoFields(int field1, int field2, boolean asc) {
        Comparator<Animal> comp1 = getComparator(field1);
        Comparator<Animal> comp2 = getComparator(field2);

        if (comp1 == null || comp2 == null) {
            System.out.println("Неверный выбор полей.");
            return;
        }

        Comparator<Animal> combined = comp1.thenComparing(comp2);
        if (!asc) combined = combined.reversed();

        animals = animals.stream()
                .sorted(combined)
                .collect(Collectors.toList()); //коллекторс чтобы список остался изменяемым

        System.out.println("Список отсортирован по двум полям.");
        printAll();
        Journal.log("Сортировка по двум полям (" + field1 + ", " + field2 + ", ASC: " + asc + ")");
    }

//stats
    public void showStats() {
    if (animals.isEmpty()) {
        System.out.println("Ферма пуста, статистики нет.");
        return;
    }

    System.out.println("\n--- СТАТИСТИКА ФЕРМЫ ---");
    System.out.println("Всего животных: " + animals.size());

    //сколько каждого типа
    int cowCount = 0, chickenCount = 0, pigCount = 0;
    for (Animal a : animals) {
        switch (a.getType()) {
            case COW: cowCount++; break;
            case CHICKEN: chickenCount++; break;
            case PIG: pigCount++; break;
        }
    }
    System.out.println("  Коров: " + cowCount);
    System.out.println("  Куриц: " + chickenCount);
    System.out.println("  Свиней: " + pigCount);

    //стата по весу
    double totalWeight = 0;
    int minWeight = Integer.MAX_VALUE;
    int maxWeight = Integer.MIN_VALUE;
    String minWeightName = "";
    String maxWeightName = "";

    for (Animal a : animals) {
        totalWeight += a.getWeight();
        if (a.getWeight() < minWeight) {
            minWeight = a.getWeight();
            minWeightName = a.getName();
        }
        if (a.getWeight() > maxWeight) {
            maxWeight = a.getWeight();
            maxWeightName = a.getName();
        }
    }
    System.out.println("\nСредний вес: " + (totalWeight / animals.size()) + " кг");
    System.out.println("Мин. вес: " + minWeight + " кг (" + minWeightName + ")");
    System.out.println("Макс. вес: " + maxWeight + " кг (" + maxWeightName + ")");

    //стата по возрасту
    double totalAge = 0;
    int minAge = Integer.MAX_VALUE;
    int maxAge = Integer.MIN_VALUE;
    String minAgeName = "";
    String maxAgeName = "";

    for (Animal a : animals) {
        totalAge += a.getAge();
        if (a.getAge() < minAge) {
            minAge = a.getAge();
            minAgeName = a.getName();
        }
        if (a.getAge() > maxAge) {
            maxAge = a.getAge();
            maxAgeName = a.getName();
        }
    }
    System.out.println("\nСредний возраст: " + (totalAge / animals.size()) + " лет");
    System.out.println("Мин. возраст: " + minAge + " лет (" + minAgeName + ")");
    System.out.println("Макс. возраст: " + maxAge + " лет (" + maxAgeName + ")");

    //средня продуктивность
    double totalProd = 0;
    for (Animal a : animals) {
        totalProd += a.getProductAmount();
    }
    System.out.println("\nСредняя продуктивность: " + (totalProd / animals.size()));

    //топ3 продуктивности
    System.out.println("\n--- ТОП-3 САМЫХ ПРОДУКТИВНЫХ ---");
    List<Animal> top3 = animals.stream()
            .sorted(Comparator.comparingDouble(Productive::getProductAmount).reversed())
            .limit(3)
            .toList();

    for (int i = 0; i < top3.size(); i++) {
        Animal a = top3.get(i);
        System.out.println((i + 1) + ". " + a.getName() + " (" + a.getType() + ") -> "
                + a.getProductAmount() + " (" + a.getProductType() + ")");
    }

    Journal.log("Запрошена статистика");
}

//files
    public void saveBin(String filename) {
        //OOS переводит в байты, FOS записывает байты
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            //может записать сразу всю коллекцию тк умный
            oos.writeObject(animals);
            System.out.println("Данные успешно сохранены в файл: " + filename);
            Journal.log("Сохранение в .dat");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadBin(String filename) {
        //OIS из байтов собирает объект, FIS считывает байты
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            //считал коллекцию в объект типа Object (самый базовый)
            Object obj = ois.readObject();
            if (obj instanceof List) { //проверка норм ли распакует это в список
                animals = (List<Animal>) obj;
                System.out.println("Данные загружены из файла: " + filename);
                Journal.log("Загрузка из .dat");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден (возможно, это первый запуск).");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }

    public void exportToCsv(String filename) {
        //PW имеет удобные методы (обертка), FW пишет в файл
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Animal a : animals) {
                writer.println(a.getType() + ";" + a.getName() + ";" + a.getGender() + ";" +
                        a.getWeight() + ";" + a.getAge() + ";" + a.getId() + ";" + a.getProductAmount());
            }
            System.out.println("Экспорт в TXT завершен: " + filename);
            Journal.log("Экспорт в .txt");
        } catch (IOException e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    public void importFromCsv(String filename) {
        //BR умеет читать построчно (обертка), FR читает из файла
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 7) continue;

                String typeStr = parts[0].trim();
                String name = parts[1].trim();
                String gender = parts[2].trim();
                int weight = Integer.parseInt(parts[3].trim());
                int age = Integer.parseInt(parts[4].trim());
                int id = Integer.parseInt(parts[5].trim());
                double special = Double.parseDouble(parts[6].trim());

                String typeKey = "";
                if (typeStr.equals("Корова") || typeStr.equalsIgnoreCase("COW")) typeKey = "COW";
                else if (typeStr.equals("Курица") || typeStr.equalsIgnoreCase("CHICKEN")) typeKey = "CHICKEN";
                else if (typeStr.equals("Свинья") || typeStr.equalsIgnoreCase("PIG")) typeKey = "PIG";
                else continue;

                String genderKey = "";
                if (gender.equals("Самец") || gender.equalsIgnoreCase("MALE")) genderKey = "MALE";
                else if (gender.equals("Самка") || gender.equalsIgnoreCase("FEMALE")) genderKey = "FEMALE";
                else continue;

                switch (typeKey) {
                    case "COW":
                        animals.add(new Cow(name, typeKey, genderKey, weight, age, id, special));
                        count++;
                        break;
                    case "CHICKEN":
                        animals.add(new Chicken(name, typeKey, genderKey, weight, age, id, (int) special));
                        count++;
                        break;
                    case "PIG":
                        animals.add(new Pig(name, typeKey, genderKey, weight, age, id, (int) special));
                        count++;
                        break;
                }
            }

            System.out.println("Импортировано животных: " + count);
            Journal.log("Импорт из CSV: " + filename + " (" + count + " записей)");

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата данных в файле.");
        }
    }
}