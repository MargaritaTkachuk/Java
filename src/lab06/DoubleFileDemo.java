package lab06;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Робота з бінарним файлом дійсних чисел.
 */
class DoubleFile {

    private final String fileName;

    public DoubleFile(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Створює (перезаписує) файл з переданих чисел.
     */
    public void createFile(double[] values) throws IOException {
        DataOutputStream out =
                new DataOutputStream(new FileOutputStream(fileName));
        for (double value : values) {
            out.writeDouble(value);
        }
        out.close();
    }

    /**
     * Читає всі числа до кінця файлу і повертає їх суму.
     */
    public double readSum() throws IOException {
        double sum = 0;
        DataInputStream in =
                new DataInputStream(new FileInputStream(fileName));
        while (in.available() >= 8) {
            sum += in.readDouble();
        }
        in.close();
        return sum;
    }
}

public class DoubleFileDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ім'я бінарного файлу: ");
        DoubleFile file = new DoubleFile(reader.readLine().trim());

        System.out.print("Скільки чисел записати у файл? ");
        int n = Integer.parseInt(reader.readLine().trim());

        System.out.println("Введіть " + n + " чисел через пробіл (з крапкою):");
        String[] parts = reader.readLine().trim().split("\\s+");
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            values[i] = Double.parseDouble(parts[i]);
        }

        file.createFile(values);
        System.out.println("Сума чисел з файлу: " + file.readSum());
    }
}
