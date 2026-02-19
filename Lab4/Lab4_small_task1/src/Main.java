import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        List c1 = new List();
        List c2 = new List();
        String[] ints = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                System.out.println("Файл пустой!");
                return;
            }
            ints = line.split(" ");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int i = 0;
        // Читаем первый набор (до отрицательного числа)
        while (i < ints.length) {
            int num = Integer.parseInt(ints[i]);
            if (num < 0) {
                i++; // пропускаем отрицательное число
                break;
            }
            c1.add(num);
            i++;
        }

        // Читаем второй набор (после отрицательного числа)
        while (i < ints.length) {
            int num = Integer.parseInt(ints[i]);
            if (num > 0) {
                c2.add(num);
            }
            i++;
        }
        c1.sort();
        c2.sort();

        System.out.print("C1: ");
        c1.print();
        System.out.print("C2: ");
        c2.print();

        List c3 = List.merge(c1, c2);

        System.out.print("C3: ");
        c3.print();
    }
}