package lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Вставляє слово "very" перед словами "small" та "large".
 */
class VeryAdder {

    private final String text;

    public VeryAdder(String text) {
        this.text = text;
    }

    public String addVery() {
        return text.replaceAll("\\b(small|large)\\b", "very $1");
    }
}

public class VeryDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введіть рядок:");
        String line = reader.readLine();

        VeryAdder adder = new VeryAdder(line);
        System.out.println("Результат:");
        System.out.println(adder.addVery());
    }
}
