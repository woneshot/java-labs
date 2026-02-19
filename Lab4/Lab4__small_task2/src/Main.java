import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Car> cars = new ArrayList<>();

        System.out.print("Количество автомобилей: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Авто " + (i + 1) + ": ");
            System.out.print("Введите стартовую позицию (км): ");
            double position = sc.nextDouble();
            System.out.print("Введите скорость (км/ч): ");
            double speed = sc.nextDouble();
            cars.add(new Car(position, speed));
        }

        System.out.println(cars.toString());

        int overtakings = 0;
        for (int i = 0; i < cars.size(); i++)
            for (int j = i + 1; j < cars.size(); j++) {
                double posI = cars.get(i).getPosition();
                double posJ = cars.get(j).getPosition();
                double endI = posI + cars.get(i).getSpeed() * 10;
                double endJ = posJ + cars.get(j).getSpeed() * 10;

                if ((posI < posJ && endI > endJ) || (posI > posJ && endI < endJ)) {
                    overtakings++;
                }
            }

        System.out.println("Общее кол-во обгонов: " + overtakings);
        // Тут будет решение
    }
}