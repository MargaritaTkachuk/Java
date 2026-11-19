package lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Очищає рядки від сміття й додає числа, що в них містяться.
 */
class GarbageCleaner {

    private static final Pattern DIGITS = Pattern.compile("\\d+");

    /**
     * Знаходить у рядку всі групи цифр підряд і повертає їх суму.
     */
    public int cleanAndSum(String line) {
        int sum = 0;
        Matcher matcher = DIGITS.matcher(line);
        while (matcher.find()) {
            sum += Integer.parseInt(matcher.group());
        }
        return sum;
    }

    /**
     * Повертає суму чисел, отриманих з усіх рядків масиву.
     */
    public int sumAll(String[] lines) {
        int total = 0;
        for (String line : lines) {
            total += cleanAndSum(line);
        }
        return total;
    }
}

public class GarbageDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Скільки рядків вводити? ");
        int n = Integer.parseInt(reader.readLine().trim());

        String[] lines = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.println("Рядок " + (i + 1) + ":");
            lines[i] = reader.readLine();
        }

        GarbageCleaner cleaner = new GarbageCleaner();

        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println("Число з рядка " + (i + 1) + ": "
                    + cleaner.cleanAndSum(lines[i]));
        }
        System.out.println("Результат: " + cleaner.sumAll(lines));
    }
}
