package lab07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Stack;

class Record {
    private final int id;
    private final String name;
    private final double value;

    public Record(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public void writeTo(RandomAccessFile file) throws IOException {
        file.writeInt(id);
        file.writeUTF(name);
        file.writeDouble(value);
    }

    public static Record readFrom(RandomAccessFile file) throws IOException {
        int id = file.readInt();
        String name = file.readUTF();
        double value = file.readDouble();
        return new Record(id, name, value);
    }

    @Override
    public String toString() {
        return "Record{id=" + id + ", name='" + name + "', value=" + value + "}";
    }
}

class RecordIndex {
    private final Stack<Long> offsets = new Stack<>();

    public void add(long offset) {
        offsets.push(offset);
    }

    public long get(int recordNumber) {
        return offsets.get(recordNumber);
    }

    public long removeLast() {
        return offsets.pop();
    }

    public int size() {
        return offsets.size();
    }

    public boolean isEmpty() {
        return offsets.isEmpty();
    }

    // Послідовно проходить файл і заново будує індекс
    public void rebuild(RandomAccessFile file) throws IOException {
        offsets.clear();
        file.seek(0);
        while (file.getFilePointer() < file.length()) {
            offsets.push(file.getFilePointer());
            Record.readFrom(file);
        }
    }
}

public class Task7_6 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        RandomAccessFile file = new RandomAccessFile("records.bin", "rw");
        RecordIndex index = new RecordIndex();

        index.rebuild(file);
        System.out.println("Індекс побудовано. Записів у файлі: " + index.size());

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1 - додати запис");
            System.out.println("2 - прочитати запис за номером");
            System.out.println("3 - показати всі записи");
            System.out.println("4 - видалити останній запис");
            System.out.println("5 - перебудувати індекс зі файлу");
            System.out.println("0 - вихід");
            System.out.print("Ваш вибір: ");
            String choice = reader.readLine().trim();

            if (choice.equals("1")) {
                System.out.print("id (ціле): ");
                int id = Integer.parseInt(reader.readLine().trim());
                System.out.print("name: ");
                String name = reader.readLine();
                System.out.print("value (дійсне): ");
                double value = Double.parseDouble(reader.readLine().trim());

                long offset = file.length();
                file.seek(offset);
                new Record(id, name, value).writeTo(file);
                index.add(offset);
                System.out.println("Додано запис №" + (index.size() - 1) + ", зсув " + offset);

            } else if (choice.equals("2")) {
                System.out.print("Номер запису (з 0): ");
                int number = Integer.parseInt(reader.readLine().trim());
                if (number < 0 || number >= index.size()) {
                    System.out.println("Немає запису з таким номером. Усього записів: " + index.size());
                } else {
                    file.seek(index.get(number));
                    System.out.println(Record.readFrom(file));
                }

            } else if (choice.equals("3")) {
                if (index.isEmpty()) {
                    System.out.println("Файл порожній.");
                }
                for (int i = 0; i < index.size(); i++) {
                    file.seek(index.get(i));
                    System.out.println(i + " (зсув " + index.get(i) + "): " + Record.readFrom(file));
                }

            } else if (choice.equals("4")) {
                if (index.isEmpty()) {
                    System.out.println("Видаляти нічого.");
                } else {
                    long offset = index.removeLast();
                    file.setLength(offset);
                    System.out.println("Останній запис видалено, файл обрізано до " + offset + " байтів.");
                }

            } else if (choice.equals("5")) {
                index.rebuild(file);
                System.out.println("Індекс перебудовано. Записів: " + index.size());

            } else if (choice.equals("0")) {
                running = false;

            } else {
                System.out.println("Невідома команда.");
            }
        }

        file.close();
    }
}
