package service;

import model.Vaccination;
import utils.Journal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VaccinationJournal {

    private List<Vaccination> vaccinations = new ArrayList<>();

//add
    public void addVaccination(String animalName, String vaccineName, String date, FarmService farmService) {
        if (!farmService.hasAnimal(animalName)) {
            System.out.println("Ошибка: Животное с именем \"" + animalName + "\" не найдено на ферме.");
            System.out.println("Сначала добавьте животное, потом делайте вакцинацию.");
            return;
        }

        Vaccination v = new Vaccination(animalName, vaccineName, date);
        vaccinations.add(v);
        System.out.println("Вакцинация записана: " + v);
        Journal.log("Вакцинация: " + animalName + " — " + vaccineName + " (" + date + ")");
    }

//print
    public void printAll() {
        if (vaccinations.isEmpty()) {
            System.out.println("Журнал вакцинаций пуст.");
            return;
        }
        System.out.println("\n=== ЖУРНАЛ ВАКЦИНАЦИЙ ===");
        for (Vaccination v : vaccinations) {
            System.out.println(v);
        }
        System.out.println("=========================");
    }

    public void printByAnimal(String animalName) {
        boolean found = false;
        System.out.println("Вакцинации для: " + animalName);
        for (Vaccination v : vaccinations) {
            if (v.getAnimalName().equalsIgnoreCase(animalName)) {
                System.out.println(v);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Записей не найдено.");
        }
    }

 //file
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(vaccinations);
            System.out.println("Журнал вакцинаций сохранён в: " + filename);
            Journal.log("Сохранение журнала вакцинаций");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                vaccinations = (List<Vaccination>) obj;
                System.out.println("Журнал вакцинаций загружен из: " + filename);
                Journal.log("Загрузка журнала вакцинаций");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл журнала не найден (возможно, первый запуск).");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}