import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int size;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Введите размерность массива: ");
            String sizeString = sc.nextLine();
            try {
                size = Integer.parseInt(sizeString); //стринг в инт
                if (size > 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели не число. Попробуйте еще раз.");
                continue;
            }
            System.out.println("Вы ввели некорректное число. Попробуйте еще раз.");
        }
        ArrayService service = new ArrayService(size);

        while (true) {
            System.out.println("\n1. Random | 2. Manual | 3. Min | 4. Max | 5. Avg | 6. Print | 7. Clear | 0. Exit");

            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        service.fillRandomly();
                        System.out.println("Готово.");
                        break;
                    case "2":
                        service.fillManually();
                        break;
                    case "3":
                        System.out.println("Min: " + service.findMinValue());
                        break;
                    case "4":
                        System.out.println("Max: " + service.findMaxValue());
                        break;
                    case "5":
                        System.out.println("Avg: " + service.findAvgValue());
                        break;
                    case "6":
                        System.out.println(service.toString());
                        break;
                    case "7":
                        service.clearArray();
                        System.out.println("Очищено.");
                        break;
                    case "0":
                        System.out.println("Выход.");
                        return; // выходим из main, а не из switch
                    default:
                        System.out.println("Неверная команда");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}