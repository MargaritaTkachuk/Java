package lab06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Робота зі словами текстового файлу.
 */
class TextFileProcessor {

    private final String fileName;

    public TextFileProcessor(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Повертає кількість слів у файлі.
     */
    public int countWords() throws IOException {
        int count = 0;
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = in.readLine();
        while (line != null) {
            String trimmed = line.trim();
            if (trimmed.length() > 0) {
                count += trimmed.split("\\s+").length;
            }
            line = in.readLine();
        }
        in.close();
        return count;
    }

    /**
     * Видаляє зайві пропуски та всі слова з однієї літери.
     * Результат записується в той самий файл.
     */
    public void cleanFile() throws IOException {
        ArrayList<String> result = new ArrayList<String>();

        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = in.readLine();
        while (line != null) {
            result.add(cleanLine(line));
            line = in.readLine();
        }
        in.close();

        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        for (String s : result) {
            out.println(s);
        }
        out.close();
    }

    /**
     * Очищає один рядок: лишає слова довші за одну літеру,
     * між ними ставить рівно один пропуск.
     */
    private String cleanLine(String line) {
        String trimmed = line.trim();
        if (trimmed.length() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String[] words = trimmed.split("\\s+");
        for (String word : words) {
            boolean singleLetter = word.length() == 1
                    && Character.isLetter(word.charAt(0));
            if (singleLetter) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * Виводить вміст файлу на консоль.
     */
    public void print() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = in.readLine();
        while (line != null) {
            System.out.println(line);
            line = in.readLine();
        }
        in.close();
    }
}

public class TextFileDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ім'я файлу: ");
        String fileName = reader.readLine().trim();

        TextFileProcessor processor = new TextFileProcessor(fileName);

        System.out.println("Слів до чистки: " + processor.countWords());

        processor.cleanFile();

        System.out.println("Слів після чистки: " + processor.countWords());
        System.out.println("\nВміст файлу після чистки:");
        processor.print();
    }
}
