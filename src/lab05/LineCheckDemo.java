package lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Перевірка рядка з цифр та літер на три властивості.
 */
class LineChecker {

    private final String line;

    public LineChecker(String line) {
        this.line = line;
    }

    /**
     * Чи складається рядок лише з цифр та літер (і він непорожній).
     */
    public boolean isLettersAndDigitsOnly() {
        if (line.length() == 0) {
            return false;
        }
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isLetterOrDigit(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Властивість 1: спочатку ненульова цифра d, потім рівно d літер.
     */
    public boolean isDigitThenLetters() {
        char first = line.charAt(0);
        if (!Character.isDigit(first) || first == '0') {
            return false;
        }
        for (int i = 1; i < line.length(); i++) {
            if (!Character.isLetter(line.charAt(i))) {
                return false;
            }
        }
        return line.length() - 1 == first - '0';
    }

    /**
     * Властивість 2: у рядку рівно одна цифра, її значення дорівнює довжині рядка.
     */
    public boolean isSingleDigitEqualsLength() {
        int digitCount = 0;
        int digitValue = 0;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isDigit(ch)) {
                digitCount++;
                digitValue = ch - '0';
            }
        }
        return digitCount == 1 && digitValue == line.length();
    }

    /**
     * Властивість 3: усі символи цифри, і вони утворюють арифметичну прогресію.
     */
    public boolean isArithmeticProgression() {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isDigit(line.charAt(i))) {
                return false;
            }
        }
        if (line.length() <= 2) {
            return true;
        }
        int diff = line.charAt(1) - line.charAt(0);
        for (int i = 2; i < line.length(); i++) {
            if (line.charAt(i) - line.charAt(i - 1) != diff) {
                return false;
            }
        }
        return true;
    }
}

public class LineCheckDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть рядок:");
        String line = reader.readLine().trim();

        LineChecker checker = new LineChecker(line);

        if (!checker.isLettersAndDigitsOnly()) {
            System.out.println("Рядок містить не лише цифри та літери (або порожній)");
            return;
        }

        System.out.println("Рядок містить лише цифри та літери");
        System.out.println("1) ненульова цифра + стільки літер, яка цифра: "
                + checker.isDigitThenLetters());
        System.out.println("2) одна цифра, що дорівнює довжині рядка: "
                + checker.isSingleDigitEqualsLength());
        System.out.println("3) цифри утворюють арифметичну прогресію: "
                + checker.isArithmeticProgression());
    }
}
