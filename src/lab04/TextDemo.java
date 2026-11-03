package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Word {
    private String value;

    Word(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

class Sentence {
    private ArrayList<Word> words = new ArrayList<Word>();

    // Створюємо речення з рядка: розбиваємо по пробілах, кожне слово стає об'єктом Word
    Sentence(String line) {
        String[] parts = line.trim().split("\\s+");
        for (String part : parts) {
            words.add(new Word(part));
        }
    }

    int wordCount() {
        return words.size();
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                result = result + " ";
            }
            result = result + words.get(i);
        }
        return result + ".";
    }
}

class Text {
    private Sentence title;
    private ArrayList<Sentence> sentences = new ArrayList<Sentence>();

    Text(Sentence title) {
        this.title = title;
    }

    // доповнити текст
    void add(Sentence sentence) {
        sentences.add(sentence);
    }

    // заголовок тексту
    Sentence getTitle() {
        return title;
    }

    // вивести текст на консоль
    void print() {
        System.out.println("=== " + title + " ===");
        String body = "";
        for (Sentence s : sentences) {
            body = body + s + " ";
        }
        System.out.println(body.trim());
    }
}

public class TextDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть заголовок (слова через пробіл, без крапки):");
        Text text = new Text(new Sentence(reader.readLine()));

        System.out.print("Скільки речень ввести? ");
        int n = Integer.parseInt(reader.readLine().trim());

        for (int i = 1; i <= n; i++) {
            System.out.println("Речення " + i + " (без крапки в кінці):");
            text.add(new Sentence(reader.readLine()));
        }

        System.out.println();
        text.print();

        System.out.println("\nДоповнюємо текст. Введіть ще одне речення:");
        text.add(new Sentence(reader.readLine()));

        System.out.println();
        text.print();
        System.out.println("\nЗаголовок: " + text.getTitle());
    }
}
