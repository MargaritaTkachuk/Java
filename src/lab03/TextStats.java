package lab03;

import java.util.Scanner;

public class TextStats {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int chars = 0;
        int words = 0;
        int lines = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines++;
            chars += line.length();

            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                words += trimmed.split("\\s+").length;
            }
        }

        System.out.println("Символів: " + chars);
        System.out.println("Слів: " + words);
        System.out.println("Рядків: " + lines);
    }
}
