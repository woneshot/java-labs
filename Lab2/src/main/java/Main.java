import model.Chicken;
import model.Cow;
import model.Pig;
import service.FarmService;

import java.util.Scanner;

public class Main {
    private static final String ADMIN_PASSWORD = "admin";

    public static void main(String[] args) {
        FarmService service = new FarmService();
        Scanner sc = new Scanner(System.in);
        String filename = "animals.txt";

        System.out.println("Добро пожаловать в систему управления фермой!");

        boolean isAdmin = false;

        while (true) {
            System.out.println("\nВыберите роль:");
            System.out.println("1. Администратор (Полный доступ)");
            System.out.println("2. Пользователь (Только просмотр)");
            System.out.print("Ваш выбор: ");

            String roleInput = sc.nextLine().trim();

            if (roleInput.equals("1")) {
                System.out.print("Введите пароль: ");
                String pass = sc.nextLine();

                if (pass.equals(ADMIN_PASSWORD)) {
                    isAdmin = true;
                    System.out.println("Вы вошли как Администратор.");
                    break;
                } else {
                    System.out.println("Неверный пароль!");
                }
            } else if (roleInput.equals("2")) {
                System.out.println("Вы вошли как Пользователь.");
                break;
            } else {
                System.out.println("Введите 1 или 2.");
            }
        }

        while (true) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");

            if (isAdmin) {
                System.out.println("1.  Добавить животное");
            } else {
                System.out.println("1.  Добавить животное (нет доступа)");
            }

            System.out.println("2.  Найти животное по кличке");
            System.out.println("3.  Показать всех животных");
            System.out.println("4.  Фильтр по Типу");
            System.out.println("5.  Фильтр по Возрасту или Весу");
            System.out.println("6.  Сортировка списка");
            System.out.println("7.  Статистика");
            System.out.println("8.  Работа с файлом (Сохранить/Загрузить)");

            if (isAdmin) {
                System.out.println("9.  Удалить животное");
                System.out.println("10. Редактировать животное (Возраст/Вес)");
            } else {
                System.out.println("9.  Удалить животное (нет доступа)");
                System.out.println("10. Редактировать животное (нет доступа)");
            }

            System.out.println("0.  Выход");
            System.out.print("Выберите пункт меню: ");

            String input = sc.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите целое число.");
                continue;
            }

            switch (choice) {
                case 1:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен. Нужны права администратора.");
                        break;
                    }
                    System.out.println("--- Добавление животного ---");
                    System.out.print("Введите тип (COW, CHICKEN, PIG): ");
                    String type = sc.nextLine().toUpperCase().trim();

                    if (!type.equals("COW") && !type.equals("CHICKEN") && !type.equals("PIG")) {
                        System.out.println("Ошибка: Неизвестный тип животного.");
                        break;
                    }

                    System.out.print("Введите кличку: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Ошибка: Имя не может быть пустым.");
                        break;
                    }
                    if (service.isNameTaken(name)) {
                        System.out.println("Ошибка: Животное с именем \"" + name + "\" уже существует.");
                        break;
                    }

                    System.out.print("Введите пол (MALE/FEMALE): ");
                    String gender = sc.nextLine().toUpperCase().trim();
                    if (!gender.equals("MALE") && !gender.equals("FEMALE")) {
                        System.out.println("Ошибка: Неверный пол. Доступны MALE, FEMALE.");
                        break;
                    }

                    int weight, age, id;
                    try {
                        System.out.print("Введите вес (кг): ");
                        weight = Integer.parseInt(sc.nextLine());

                        System.out.print("Введите возраст (лет): ");
                        age = Integer.parseInt(sc.nextLine());

                        System.out.print("Введите ID (число): ");
                        id = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введено не число! Операция отменена.");
                        break;
                    }

                    if (service.isIdTaken(id)) {
                        System.out.println("Ошибка: Животное с ID=" + id + " уже существует.");
                        break;
                    }

                    try {
                        if (type.equals("COW")) {
                            System.out.print("Удой молока (литров в день): ");
                            double milk = Double.parseDouble(sc.nextLine());
                            service.addAnimal(new Cow(type, gender, weight, age, name, id, milk));
                            System.out.println("Корова успешно добавлена!");
                        } else if (type.equals("CHICKEN")) {
                            System.out.print("Яиц в день (шт): ");
                            int eggs = Integer.parseInt(sc.nextLine());
                            service.addAnimal(new Chicken(type, gender, weight, age, name, id, eggs));
                            System.out.println("Курица успешно добавлена!");
                        } else  {
                            System.out.print("Процент жира (%): ");
                            int fat = Integer.parseInt(sc.nextLine());
                            service.addAnimal(new Pig(type, gender, weight, age, name, id, fat));
                            System.out.println("Свинья успешно добавлена!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введено не число! Операция отменена.");
                    }
                    break;

                case 2:
                    System.out.print("Введите кличку для поиска: ");
                    String searchName = sc.nextLine();
                    service.searchByName(searchName);
                    break;

                case 3:
                    System.out.println("--- Список всех животных ---");
                    service.printAllAnimals();
                    break;

                case 4:
                    System.out.print("Введите тип для фильтра (COW/CHICKEN/PIG): ");
                    String filterType = sc.nextLine();
                    service.filterByType(filterType);
                    break;

                case 5:
                    System.out.println("1 - Фильтр по Возрасту");
                    System.out.println("2 - Фильтр по Весу");
                    System.out.print("Ваш выбор: ");

                    try {
                        int fChoice = Integer.parseInt(sc.nextLine());

                        if (fChoice == 1) {
                            System.out.print("Введите искомый возраст: ");
                            int fAge = Integer.parseInt(sc.nextLine());
                            service.filterByAge(fAge);
                        } else if (fChoice == 2) {
                            System.out.print("Введите искомый вес: ");
                            int fWeight = Integer.parseInt(sc.nextLine());
                            service.filterByWeight(fWeight);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите число!");
                    }
                    break;

                case 6:
                    System.out.println("1 - Сортировать по Имени");
                    System.out.println("2 - Сортировать по Весу");
                    System.out.print("Ваш выбор: ");

                    try {
                        int sChoice = Integer.parseInt(sc.nextLine());

                        if (sChoice == 1) {
                            service.sortByName();
                            System.out.println("Список отсортирован по имени.");
                        } else if (sChoice == 2) {
                            service.sortByWeight();
                            System.out.println("Список отсортирован по весу.");
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите число!");
                    }
                    break;

                case 7:
                    System.out.print("Введите тип для статистики (COW/CHICKEN/PIG): ");
                    String statType = sc.nextLine();
                    service.statsByType(statType);
                    break;

                case 8:
                    System.out.println("1 - Сохранить в файл (" + filename + ")");
                    System.out.println("2 - Загрузить из файла (" + filename + ")");

                    try {
                        int fileChoice = Integer.parseInt(sc.nextLine());

                        if (fileChoice == 1) {
                            service.saveToFile(filename);
                        } else if (fileChoice == 2) {
                            service.loadFromFile(filename);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите 1 или 2.");
                    }
                    break;

                case 9:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен. Нужны права администратора.");
                        break;
                    }
                    System.out.print("Введите имя животного для удаления: ");
                    String delName = sc.nextLine();
                    service.deleteAnimalByName(delName);
                    break;

                case 10:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен. Нужны права администратора.");
                        break;
                    }
                    System.out.print("Введите имя животного для редактирования: ");
                    String editName = sc.nextLine();

                    System.out.println("Что хотите изменить?");
                    System.out.println("1 - Возраст");
                    System.out.println("2 - Вес");

                    try {
                        int editChoice = Integer.parseInt(sc.nextLine());

                        if (editChoice == 1) {
                            service.editAgeByName(editName, sc);
                        } else if (editChoice == 2) {
                            service.editWeightByName(editName, sc);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите 1 или 2.");
                    }
                    break;

                case 0:
                    System.out.println("Завершение работы программы. До свидания!");
                    return;

                default:
                    System.out.println("Такой команды нет. Попробуйте еще раз.");
            }
        }
    }
}