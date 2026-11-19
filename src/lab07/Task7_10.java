package lab07;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Task7_10 {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість точок): ");
        int n = Integer.parseInt(console.readLine().trim());

        // LinkedHashSet прибирає повторювані точки й зберігає порядок введення
        Set<Point> unique = new LinkedHashSet<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Точка " + (i + 1) + " (x y): ");
            String[] parts = console.readLine().trim().split("\\s+");
            unique.add(new Point(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }
        List<Point> points = new ArrayList<>(unique);
        int m = points.size();

        System.out.print("Ім'я вихідного файлу: ");
        String outputName = console.readLine().trim();

        // Для кожної прямої рахуємо, скільки пар точок на ній лежить
        Map<Line, Integer> pairCounts = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                Line line = Line.through(points.get(i), points.get(j));
                Integer current = pairCounts.get(line);
                if (current == null) {
                    pairCounts.put(line, 1);
                } else {
                    pairCounts.put(line, current + 1);
                }
            }
        }

        // Якщо на прямій p точок, то пар на ній p(p-1)/2 = k, звідки p = (1 + sqrt(1 + 8k)) / 2
        List<Map.Entry<Line, Integer>> entries = new ArrayList<>(pairCounts.entrySet());
        entries.sort((first, second) -> second.getValue() - first.getValue());

        PrintWriter writer = new PrintWriter(new FileWriter(outputName));
        writer.println("Різних точок: " + m + (m < n ? " (повтори прибрано, введено " + n + ")" : ""));
        writer.println("Прямих, що проходять більш ніж через одну точку: " + entries.size());
        writer.println();
        for (Map.Entry<Line, Integer> entry : entries) {
            long k = entry.getValue();
            long pointsOnLine = Math.round((1 + Math.sqrt(1 + 8.0 * k)) / 2);
            writer.println(entry.getKey() + " - точок: " + pointsOnLine);
        }
        writer.close();

        System.out.println("Готово. Знайдено прямих: " + entries.size() + ". Результат у файлі " + outputName);
    }
}

class Point {
    final long x;
    final long y;

    Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Point)) {
            return false;
        }
        Point p = (Point) other;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

// Пряма A*x + B*y + C = 0 у канонічному вигляді (взаємно прості цілі коефіцієнти, перший ненульовий з A, B додатний)
class Line {
    final long a;
    final long b;
    final long c;

    private Line(long a, long b, long c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    static Line through(Point p, Point q) {
        long a = q.y - p.y;
        long b = p.x - q.x;
        long c = q.x * p.y - p.x * q.y;

        long g = gcd(gcd(Math.abs(a), Math.abs(b)), Math.abs(c));
        a /= g;
        b /= g;
        c /= g;

        if (a < 0 || (a == 0 && b < 0)) {
            a = -a;
            b = -b;
            c = -c;
        }
        return new Line(a, b, c);
    }

    private static long gcd(long u, long v) {
        while (v != 0) {
            long t = u % v;
            u = v;
            v = t;
        }
        return u;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Line)) {
            return false;
        }
        Line l = (Line) other;
        return a == l.a && b == l.b && c == l.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (a != 0) {
            sb.append(a == 1 ? "x" : a + "x");
        }
        if (b != 0) {
            if (sb.length() == 0) {
                sb.append(b == 1 ? "y" : b + "y");
            } else {
                sb.append(b < 0 ? " - " : " + ").append(Math.abs(b) == 1 ? "y" : Math.abs(b) + "y");
            }
        }
        if (c != 0) {
            sb.append(c < 0 ? " - " : " + ").append(Math.abs(c));
        }
        return sb.append(" = 0").toString();
    }
}