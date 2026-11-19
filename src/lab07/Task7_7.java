package lab07;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

class TextLines {
    private final ArrayList<String> lines = new ArrayList<>();

    // Читає всі рядки з потоку до кінця файлу
    public void load(BufferedReader source) throws IOException {
        String line = source.readLine();
        while (line != null) {
            lines.add(line);
            line = source.readLine();
        }
    }

    // Природний порядок (за кодами символів)
    public void sortNatural() {
        Collections.sort(lines);
    }

    // Алфавітний порядок з урахуванням української локалі
    public void sortUkrainian() {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));
        Collections.sort(lines, collator);
    }

    public int size() {
        return lines.size();
    }

    public String get(int index) {
        return lines.get(index);
    }
}

public class Task7_7 {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ім'я файлу: ");
        String fileName = console.readLine().trim();

        BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        TextLines text = new TextLines();
        text.load(fileReader);
        fileReader.close();

        System.out.println("Прочитано рядків: " + text.size());
        System.out.println();
        System.out.println("До сортування:");
        for (int i = 0; i < text.size(); i++) {
            System.out.println(text.get(i));
        }

        System.out.println();
        System.out.println("1 - природний порядок (Collections.sort(list))");
        System.out.println("2 - український алфавітний порядок (Collator)");
        System.out.print("Режим: ");
        String mode = console.readLine().trim();

        if (mode.equals("2")) {
            text.sortUkrainian();
        } else {
            text.sortNatural();
        }

        System.out.println();
        System.out.println("Після сортування:");
        for (int i = 0; i < text.size(); i++) {
            System.out.println(text.get(i));
        }
    }
}
