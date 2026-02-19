import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ArrayService {
    private static final Logger logger = LogManager.getLogger(ArrayService.class); // логгер
    private int[] arr;
    private boolean isFilled = false; // пустой - заполненный нулями

    public ArrayService(int[] arr) {
        if (arr != null && arr.length > 0) {
            this.arr = arr.clone();
            isFilled = true;
            logger.info("Массив создан из готовых данных. Размер: {}", arr.length);
        } else {
            this.arr = new int[1];
            logger.warn("Передан пустой массив, создана заглушка размера 1");
        }
    }

    public ArrayService(int size) {
        int finalSize = Math.max(1, size);
        this.arr = new int[finalSize];
        logger.info("Создан пустой массив под заполнение. Размер: {}", finalSize);
    }

    public void clearArray() {
        Arrays.fill(arr, 0); //класс для работы с массивами
        isFilled = false;
        logger.info("Данные массива стерты, флаг заполнения сброшен");
    }

    public void fillRandomly() throws IllegalStateException {
        if (this.isFilled) {
            logger.error("Ошибка: попытка заполнить массив без очистки");
            throw new IllegalStateException("Массив уже заполнен! Очистите его.");
        }
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(201) - 100;
        }
        isFilled = true;
        logger.info("Массив успешно забит рандомными числами");
    }

    public void fillManually() {
        if (this.isFilled) {
            logger.warn("Ручной ввод отменен: массив уже заполнен");
            System.out.println("Сначала очистите массив!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите " + arr.length + " чисел через пробел:");

        try {
            String input = sc.nextLine();
            String[] values = input.trim().split("\\s+");

            for (int i = 0; i < arr.length; i++) {
                arr[i] = Integer.parseInt(values[i]);
            }
            isFilled = true;
            logger.info("Пользователь успешно ввел данные вручную: {}", Arrays.toString(arr));
        } catch (Exception e) {
            logger.error("Ошибка при ручном вводе данных: {}", e.getMessage());
            throw new NumberFormatException("Ошибка парсинга: проверьте, что Вы вводите именно числа и в кол-ве равном или большем размерности массива");
        }
    }

    public int findMinValue() {
        if (!isFilled) {
            logger.warn("Вызов поиска мин. значения на пустом массиве");
            return 0;
        }
        /*
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) { min = arr[i]; }
        }
        return min;
        */
        int res =  Arrays.stream(arr).min().getAsInt(); //стрим апи для удобного поиска и сортировки далее
        logger.info("Найден минимальный элемент: {}", res);
        return res;
    }

    public int findMaxValue() {
        if (!isFilled) {
            logger.warn("Вызов поиска макс. значения на пустом массиве");
            return 0;
        }
        /*
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) { max = arr[i]; }
        }
        return max;
        */
        int res = Arrays.stream(arr).max().getAsInt();
        logger.info("Найден максимальный элемент: {}", res);
        return res;
    }

    public double findAvgValue() {
        if (!isFilled) {
            logger.warn("Попытка посчитать среднее в пустом массиве");
            return 0;
        }
        double avg = Arrays.stream(arr).average().getAsDouble();
        logger.info("Среднее арифметическое вычислено: {}", avg);
        return avg;

        // double sum;
        // for (int value : arr) { sum += value; }
        // double avg = sum/arr.length;
        // return avg;
    }

    @Override
    public String toString() {
        return "ArrayService{arr=" + Arrays.toString(arr) + ", isFilled=" + isFilled + "}";
    }
}