package service;

import model.*;
import model.enums.TypeEnum;
import model.enums.GenderEnum;
import interfaces.Productive;
import utils.Journal;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

//filter
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
    if (animals.isEmpty()) { System.out.println("Пусто."); return; }

    System.out.println("\n--- СТАТИСТИКА ---");
    System.out.println("Всего: " + animals.size());

    //запускает конвейер (stream), собирает в кучу результат (collect)
    //класс Collectors имеет нужные методы для работы с потоками
    //groupingBy делит на группы (кучки) по заданному типу (поле тип класса Animal)
    //counting считает сколько в каждой кучке элементов
    Map<TypeEnum, Long> counts = animals.stream()
            .collect(Collectors.groupingBy(Animal::getType, Collectors.counting()));
    //лямбда для вывода
    counts.forEach((k, v) -> System.out.println(k + ": " + v));

    /*
    запускает конвейер, собирает в кучу результат
    summarizingDouble(Animal::getWeight) - пройдись по всем, возьми их вес и закинь в калькулятор статистики:

    внутри этого калькулятора (summarizingDouble) уже лежат значения
    getAverage() — среднее
    getMin() — минимум
    getMax() — максимум
    getSum() — сумма
    getCount() — количество
    */

    DoubleSummaryStatistics wStats = animals.stream()
            .collect(Collectors.summarizingDouble(Animal::getWeight));
    //привет старый добрый C)
    //% - здесь переменная, .1 (.0) - сколько знаков после запятой, f - что будет float/double
    System.out.printf("Вес: Сред %.1f | Мин %.0f | Макс %.0f\n",
            wStats.getAverage(), wStats.getMin(), wStats.getMax());

    //нужно только среднее поэтому другой метод класса Collectors
    double avgProd = animals.stream()
            .collect(Collectors.averagingDouble(Productive::getProductAmount));
    System.out.printf("Средняя продуктивность: %.1f\n", avgProd);

    //1) идем по конвейеру
    //2) сортируем
    //3)сравниваем даблы, а именно поле кол-во продукта
    //4)реверсим, чтобы было по убыванию
    //5)максимум будет 3 элемента (топ 3)
    //6)для каждого элемента (то есть первых 3 после сортировки):
    //7)лямбда, которая берет зверушку и печатает ее поля имени и кол-ва продукта
    System.out.println("\nТОП-3 Продуктивных:");
    animals.stream()
            .sorted(Comparator.comparingDouble(Productive::getProductAmount).reversed())
            .limit(3)
            .forEach(a -> System.out.println(a.getName() + ": " + a.getProductAmount()));

    Journal.log("Статистика");
}

//files
    public void saveBin(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(animals);
            System.out.println("Сохранено.");
        } catch (IOException e) { System.out.println("Ошибка сохранения."); }
    }

    @SuppressWarnings("unchecked")
    public void loadBin(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            animals = (List<Animal>) ois.readObject();
            System.out.println("Загружено.");
        } catch (Exception e) { System.out.println("Ошибка загрузки."); }
    }

    public void exportToCsv(String filename) {
        //добавил явное указание кодировки UTF-8
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            for (Animal a : animals) {
                w.println(a.getType() + ";" + a.getName() + ";" + a.getGender() + ";" +
                        a.getWeight() + ";" + a.getAge() + ";" + a.getId() + ";" + a.getProductAmount());
            }
            System.out.println("Экспорт завершен.");
        } catch (IOException e) { System.out.println("Ошибка экспорта."); }
    }

    public void importFromCsv(String filename) {
        //добавил явное указание кодировки UTF-8
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String l;
            while ((l = r.readLine()) != null) {
                String[] p = l.split(";");
                if (p.length < 7) continue;
                // Парсинг (упрощен для краткости)
                String type = p[0], name = p[1], gen = p[2];
                int w = Integer.parseInt(p[3]), a = Integer.parseInt(p[4]), id = Integer.parseInt(p[5]);
                double prod = Double.parseDouble(p[6]);

                if (type.equals("COW")) animals.add(new Cow(name, type, gen, w, a, id, prod));
                else if (type.equals("CHICKEN")) animals.add(new Chicken(name, type, gen, w, a, id, (int)prod));
                else if (type.equals("PIG")) animals.add(new Pig(name, type, gen, w, a, id, (int)prod));
            }
            System.out.println("Импорт завершен.");
        } catch (Exception e) { System.out.println("Ошибка импорта."); }
    }



}


