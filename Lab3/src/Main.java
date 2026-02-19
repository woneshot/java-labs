import service.FarmService;
import service.VaccinationJournal;
import utils.Journal;

import java.util.Scanner;

public class Main {
    private static final String ADMIN_PASSWORD = "admin";

    public static void main(String[] args) {
        FarmService service = new FarmService();
        VaccinationJournal vacJournal = new VaccinationJournal();
        Scanner sc = new Scanner(System.in);

        String binFile = "farm.dat";
        String txtFile = "farm_export.txt";
        String vacFile = "vaccinations.dat";

        System.out.println("=== ФЕРМА ===");

        //авторизация
        boolean isAdmin = false;

        while (true) {
            System.out.println("\nВыберите роль:");
            System.out.println("1. Администратор (Полный доступ)");
            System.out.println("2. Пользователь (Только просмотр)");
            System.out.print("Ваш выбор: ");

            String roleInput = sc.nextLine().trim();

            if (roleInput.equals("1")) {
                System.out.print("Введите пароль администратора: ");
                String inputPass = sc.nextLine();

                if (inputPass.equals(ADMIN_PASSWORD)) {
                    isAdmin = true;
                    System.out.println("Пароль принят. Вы вошли как Администратор.");
                    Journal.log("Вход: АДМИНИСТРАТОР");
                    break;
                } else {
                    System.out.println("Ошибка: Неверный пароль.");
                    Journal.log("Ошибка входа (пароль)");
                }
            } else if (roleInput.equals("2")) {
                System.out.println("Вы вошли как Пользователь.");
                Journal.log("Вход: ПОЛЬЗОВАТЕЛЬ");
                break;
            } else {
                System.out.println("Ошибка: Введите 1 или 2.");
            }
        }

        //меню
        while (true) {
            System.out.println("\n--- ГЛАВНОЕ МЕНЮ ---");

            if (isAdmin) {
                System.out.println("1. Добавить животное");
                System.out.println("2. Удалить животное");
                System.out.println("3. Редактировать животное");
            } else {
                System.out.println("1-3. (Недоступно для пользователя)");
            }

            System.out.println("4. Просмотр списка");
            System.out.println("5. Поиск по имени");
            System.out.println("6. Сложный фильтр (Тип + Вес + Возраст)");
            System.out.println("7. Сортировка");
            System.out.println("8. Статистика");
            System.out.println("9. Сохранить / Загрузить (.dat)");
            System.out.println("10. Экспорт / Импорт (.txt)");
            System.out.println("11. Покормить животных");
            System.out.println("12. Журнал вакцинаций");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String input = sc.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите число!");
                continue;
            }

            switch (choice) {
                case 1:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен. Нужны права администратора.");
                        break;
                    }
                    service.createNewAnimal(sc);
                    break;

                case 2:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен.");
                        break;
                    }
                    System.out.print("Введите имя животного для удаления: ");
                    service.deleteAnimalByName(sc.nextLine());
                    break;

                case 3:
                    if (!isAdmin) {
                        System.out.println("Доступ запрещен.");
                        break;
                    }
                    System.out.print("Введите имя животного для редактирования: ");
                    service.editAnimal(sc.nextLine(), sc);
                    break;

                case 4:
                    service.printAll();
                    break;

                case 5:
                    System.out.print("Введите имя (или часть): ");
                    service.searchByName(sc.nextLine());
                    break;

                case 6:
                    System.out.println("Фильтр: Тип + Вес(диапазон) + Возраст");

                    System.out.print("Тип (COW, CHICKEN, PIG): ");
                    String fType = sc.nextLine();

                    int minW, maxW, fAge;
                    try {
                        System.out.print("Вес ОТ (кг): ");
                        minW = Integer.parseInt(sc.nextLine());

                        System.out.print("Вес ДО (кг): ");
                        maxW = Integer.parseInt(sc.nextLine());

                        System.out.print("Точный возраст (лет): ");
                        fAge = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введено не число! Фильтр отменён.");
                        break;
                    }

                    service.filterCombined(fType, minW, maxW, fAge);
                    break;

                case 7:
                    System.out.println("1 - Сортировка по одному полю");
                    System.out.println("2 - Сортировка по двум полям");

                    int sortMode;
                    try {
                        sortMode = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите число!");
                        break;
                    }

                    System.out.println("Поля: 1-Имя, 2-Вес, 3-Возраст");

                    try {
                        if (sortMode == 1) {
                            System.out.print("Поле: ");
                            int sortField = Integer.parseInt(sc.nextLine());

                            System.out.print("Порядок (1-ASC, 2-DESC): ");
                            int dir = Integer.parseInt(sc.nextLine());

                            service.sortAnimals(sortField, dir == 1);

                        } else if (sortMode == 2) {
                            System.out.print("Первое поле: ");
                            int f1 = Integer.parseInt(sc.nextLine());

                            System.out.print("Второе поле: ");
                            int f2 = Integer.parseInt(sc.nextLine());

                            System.out.print("Порядок (1-ASC, 2-DESC): ");
                            int dir = Integer.parseInt(sc.nextLine());

                            service.sortByTwoFields(f1, f2, dir == 1);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите число!");
                    }
                    break;

                case 8:
                    service.showStats();
                    break;

                case 9:
                    System.out.println("1 - Сохранить в файл");
                    System.out.println("2 - Загрузить из файла");

                    try {
                        int sl = Integer.parseInt(sc.nextLine());
                        if (sl == 1) {
                            service.saveBin(binFile);
                            vacJournal.saveToFile(vacFile);
                        } else if (sl == 2) {
                            service.loadBin(binFile);
                            vacJournal.loadFromFile(vacFile);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите 1 или 2.");
                    }
                    break;

                case 10:
                    System.out.println("1 - Экспорт в TXT");
                    System.out.println("2 - Импорт из TXT");

                    try {
                        int txtChoice = Integer.parseInt(sc.nextLine());
                        if (txtChoice == 1) {
                            service.exportToCsv(txtFile);
                        } else if (txtChoice == 2) {
                            service.importFromCsv(txtFile);
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите 1 или 2.");
                    }
                    break;

                case 11:
                    System.out.println("1 - Покормить всех");
                    System.out.println("2 - Покормить по имени");

                    try {
                        int feedChoice = Integer.parseInt(sc.nextLine());
                        if (feedChoice == 1) {
                            service.feedAll();
                        } else if (feedChoice == 2) {
                            System.out.print("Введите имя животного: ");
                            service.feedByName(sc.nextLine());
                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите 1 или 2.");
                    }
                    break;

                case 12:
                    System.out.println("\n--- ЖУРНАЛ ВАКЦИНАЦИЙ ---");
                    System.out.println("1 - Добавить запись о вакцинации");
                    System.out.println("2 - Показать все записи");
                    System.out.println("3 - Показать записи по имени животного");

                    try {
                        int vacChoice = Integer.parseInt(sc.nextLine());

                        if (vacChoice == 1) {
                            if (!isAdmin) {
                                System.out.println("Доступ запрещен. Нужны права администратора.");
                                break;
                            }
                            System.out.print("Имя животного: ");
                            String vacAnimal = sc.nextLine().trim();

                            System.out.print("Название вакцины: ");
                            String vacName = sc.nextLine().trim();

                            System.out.print("Дата (DD.MM.YYYY): ");
                            String vacDate = sc.nextLine().trim();

                            if (vacAnimal.isEmpty() || vacName.isEmpty() || vacDate.isEmpty()) {
                                System.out.println("Ошибка: Все поля должны быть заполнены.");
                            } else {
                                vacJournal.addVaccination(vacAnimal, vacName, vacDate, service);
                            }

                        } else if (vacChoice == 2) {
                            vacJournal.printAll();

                        } else if (vacChoice == 3) {
                            System.out.print("Введите имя животного: ");
                            vacJournal.printByAnimal(sc.nextLine());

                        } else {
                            System.out.println("Неверный выбор.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: Введите число.");
                    }
                    break;

                case 0:
                    System.out.println("Завершение работы.");
                    Journal.log("Выход из системы");
                    return;

                default:
                    System.out.println("Нет такой команды.");
            }
        }
    }
}