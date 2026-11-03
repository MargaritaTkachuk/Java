package lab06;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Факультет.
 */
class Faculty implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String dean;
    private final int students;

    public Faculty(String name, String dean, int students) {
        this.name = name;
        this.dean = dean;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public String getDean() {
        return dean;
    }

    public int getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Faculty other = (Faculty) o;
        return students == other.students
                && Objects.equals(name, other.name)
                && Objects.equals(dean, other.dean);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dean, students);
    }

    @Override
    public String toString() {
        return name + ", декан: " + dean + ", студентів: " + students;
    }
}

/**
 * Інститут: має масив факультетів.
 */
class Institute implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final Faculty[] faculties;

    public Institute(String name, Faculty[] faculties) {
        this.name = name;
        this.faculties = faculties.clone();
    }

    public String getName() {
        return name;
    }

    /**
     * Загальна кількість студентів на всіх факультетах.
     */
    public int getTotalStudents() {
        int total = 0;
        for (Faculty faculty : faculties) {
            total += faculty.getStudents();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Institute other = (Institute) o;
        return Objects.equals(name, other.name)
                && Arrays.equals(faculties, other.faculties);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(name) + Arrays.hashCode(faculties);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Інститут \"").append(name).append("\"\n");
        for (int i = 0; i < faculties.length; i++) {
            sb.append("  ").append(i + 1).append(") ")
                    .append(faculties[i]).append("\n");
        }
        sb.append("Усього студентів: ").append(getTotalStudents());
        return sb.toString();
    }
}

/**
 * Збереження й відновлення інституту з бінарного файлу.
 */
class StorageManager {

    private final String fileName;

    public StorageManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Серіалізація: записує об'єкт у файл.
     */
    public void save(Institute institute) throws IOException {
        ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(institute);
        out.close();
    }

    /**
     * Десеріалізація: читає об'єкт із файлу.
     */
    public Institute load() throws IOException, ClassNotFoundException {
        ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(fileName));
        Institute institute = (Institute) in.readObject();
        in.close();
        return institute;
    }
}

public class SerializationDemo {

    public static void main(String[] args)
            throws IOException, ClassNotFoundException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Назва інституту: ");
        String instituteName = reader.readLine().trim();

        System.out.print("Скільки факультетів? ");
        int n = Integer.parseInt(reader.readLine().trim());

        Faculty[] faculties = new Faculty[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Факультет " + (i + 1) + ": назва: ");
            String name = reader.readLine().trim();
            System.out.print("  декан: ");
            String dean = reader.readLine().trim();
            System.out.print("  кількість студентів: ");
            int students = Integer.parseInt(reader.readLine().trim());
            faculties[i] = new Faculty(name, dean, students);
        }

        Institute original = new Institute(instituteName, faculties);
        System.out.println("\n=== Створений об'єкт ===");
        System.out.println(original);

        StorageManager storage = new StorageManager("institute.bin");
        storage.save(original);
        System.out.println("\nОб'єкт серіалізовано у файл institute.bin");

        Institute restored = storage.load();
        System.out.println("\n=== Відновлений об'єкт ===");
        System.out.println(restored);

        System.out.println("\nТой самий об'єкт у пам'яті (==): "
                + (original == restored));
        System.out.println("Однаковий вміст (equals): "
                + original.equals(restored));
    }
}
