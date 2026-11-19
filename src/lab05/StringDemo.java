package lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Перетворення рядків. Кожен метод повертає новий рядок.
 */
class StringProcessor {

    private final String line;

    public StringProcessor(String line) {
        this.line = line;
    }

    /**
     * Видаляє цифри, подвоює знаки '+' та '-'.
     */
    public String removeDigitsAndDoubleSigns() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isDigit(ch)) {
                continue;
            }
            result.append(ch);
            if (ch == '+' || ch == '-') {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Видаляє знаки '+', безпосередньо за якими стоїть цифра.
     */
    public String removePlusBeforeDigit() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            boolean digitFollows = i + 1 < line.length()
                    && Character.isDigit(line.charAt(i + 1));
            if (ch == '+' && digitFollows) {
                continue;
            }
            result.append(ch);
        }
        return result.toString();
    }

    /**
     * Видаляє літери 'в', безпосередньо перед якими стоїть літера 'с'.
     */
    public String removeVAfterS() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            boolean sBefore = i > 0 && line.charAt(i - 1) == 'с';
            if (ch == 'в' && sBefore) {
                continue;
            }
            result.append(ch);
        }
        return result.toString();
    }

    /**
     * Замінює кожну пару "ph" на літеру 'f'.
     */
    public String replacePhWithF() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < line.length()) {
            boolean pair = line.charAt(i) == 'p'
                    && i + 1 < line.length()
                    && line.charAt(i + 1) == 'h';
            if (pair) {
                result.append('f');
                i += 2;
            } else {
                result.append(line.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    /**
     * З кількох пропусків підряд залишає один.
     */
    public String removeExtraSpaces() {
        return line.replaceAll(" +", " ");
    }
}

public class StringDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть рядок:");
        String line = reader.readLine();

        StringProcessor processor = new StringProcessor(line);

        System.out.println("\n1) Без цифр, знаки + та - подвоєно:");
        System.out.println(processor.removeDigitsAndDoubleSigns());

        System.out.println("2) Без '+', після яких цифра:");
        System.out.println(processor.removePlusBeforeDigit());

        System.out.println("3) Без 'в' після 'с':");
        System.out.println(processor.removeVAfterS());

        System.out.println("4) Пари 'ph' замінено на 'f':");
        System.out.println(processor.replacePhWithF());

        System.out.println("5) Зайві пропуски видалено:");
        System.out.println(processor.removeExtraSpaces());
    }
}
