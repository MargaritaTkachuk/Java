package lab06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Копіювання текстового файлу із заміною символів '0' і '1'.
 */
class BinarySwapper {

    /**
     * Переписує всі рядки файлу F у файл G, міняючи '0' на '1' і навпаки.
     */
    public void rewrite(String fileF, String fileG) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileF));
        PrintWriter out = new PrintWriter(new FileWriter(fileG));

        String line = in.readLine();
        while (line != null) {
            out.println(swap(line));
            line = in.readLine();
        }

        in.close();
        out.close();
    }

    /**
     * Міняє в рядку '0' на '1' і '1' на '0'.
     */
    private String swap(String line) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '0') {
                result.append('1');
            } else if (ch == '1') {
                result.append('0');
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}

public class SwapDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ім'я файлу F (звідки читати): ");
        String fileF = reader.readLine().trim();
        System.out.print("Ім'я файлу G (куди писати): ");
        String fileG = reader.readLine().trim();

        new BinarySwapper().rewrite(fileF, fileG);

        System.out.println("Готово. Вміст файлу G:");
        BufferedReader check = new BufferedReader(new FileReader(fileG));
        String line = check.readLine();
        while (line != null) {
            System.out.println(line);
            line = check.readLine();
        }
        check.close();
    }
}
