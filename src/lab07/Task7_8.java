package lab07;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

class WordFrequencyCounter {
    private final Map<String, Integer> counts = new LinkedHashMap<>();
    private long totalWords = 0;

    public void addTarget(String word) {
        String normalized = word.toLowerCase(Locale.ROOT);
        if (!normalized.isEmpty() && !counts.containsKey(normalized)) {
            counts.put(normalized, 0);
        }
    }

    // Обробляє один рядок тексту
    public void processLine(String line) {
        String[] tokens = line.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}'’ʼ]+");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            totalWords++;
            Integer current = counts.get(token);
            if (current != null) {
                counts.put(token, current + 1);
            }
        }
    }

    public long getTotalWords() {
        return totalWords;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }

    public double relativeFrequency(String word) {
        if (totalWords == 0) {
            return 0.0;
        }
        return (double) counts.get(word) / totalWords;
    }
}

public class Task7_8 {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        WordFrequencyCounter counter = new WordFrequencyCounter();

        // Список слів: з аргументів командного рядка або з консолі
        if (args.length > 0) {
            for (String arg : args) {
                counter.addTarget(arg);
            }
        } else {
            System.out.print("Слова через пробіл: ");
            String line = console.readLine();
            if (line != null) {
                for (String word : line.trim().split("[\\s,;]+")) {
                    counter.addTarget(word);
                }
            }
        }

        if (counter.getCounts().isEmpty()) {
            System.out.println("Список слів порожній.");
            return;
        }

        System.out.print("Ім'я файлу з текстом: ");
        String fileName = console.readLine().trim();

        BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        String textLine = fileReader.readLine();
        while (textLine != null) {
            counter.processLine(textLine);
            textLine = fileReader.readLine();
        }
        fileReader.close();

        System.out.println();
        System.out.println("Усього слів у тексті: " + counter.getTotalWords());
        System.out.println();
        System.out.println(String.format(Locale.ROOT, "%-20s %10s %12s %10s",
                "Слово", "Разів", "Частота", "Відсоток"));
        for (String word : counter.getCounts().keySet()) {
            int count = counter.getCounts().get(word);
            double frequency = counter.relativeFrequency(word);
            System.out.println(String.format(Locale.ROOT, "%-20s %10d %12.4f %9.2f%%",
                    word, count, frequency, frequency * 100));
        }
    }
}
