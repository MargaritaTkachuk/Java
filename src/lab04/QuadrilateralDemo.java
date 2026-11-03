package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Quadrilateral {
    static final double EPS = 1e-9;

    static final int SQUARE = 0;
    static final int RECTANGLE = 1;
    static final int RHOMBUS = 2;
    static final int ARBITRARY = 3;

    double x1, y1, x2, y2, x3, y3, x4, y4;

    Quadrilateral(double x1, double y1, double x2, double y2,
                  double x3, double y3, double x4, double y4) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.x3 = x3; this.y3 = y3;
        this.x4 = x4; this.y4 = y4;
    }

    double perimeter() {
        double ab = Math.hypot(x2 - x1, y2 - y1);
        double bc = Math.hypot(x3 - x2, y3 - y2);
        double cd = Math.hypot(x4 - x3, y4 - y3);
        double da = Math.hypot(x1 - x4, y1 - y4);
        return ab + bc + cd + da;
    }

    double area() {
        double sum = x1 * y2 - x2 * y1
                + x2 * y3 - x3 * y2
                + x3 * y4 - x4 * y3
                + x4 * y1 - x1 * y4;
        return Math.abs(sum) / 2;
    }

    int getType() {
        // паралелограм: вектор AB дорівнює вектору DC
        boolean parallelogram = Math.abs((x2 - x1) - (x3 - x4)) < EPS
                && Math.abs((y2 - y1) - (y3 - y4)) < EPS;
        if (!parallelogram) {
            return ARBITRARY;
        }

        // прямий кут при вершині A: AB · AD = 0
        double dot = (x2 - x1) * (x4 - x1) + (y2 - y1) * (y4 - y1);
        boolean rightAngle = Math.abs(dot) < EPS;

        // суміжні сторони рівні: |AB| = |AD|
        double ab = Math.hypot(x2 - x1, y2 - y1);
        double ad = Math.hypot(x4 - x1, y4 - y1);
        boolean equalSides = Math.abs(ab - ad) < EPS;

        if (rightAngle && equalSides) {
            return SQUARE;
        }
        if (rightAngle) {
            return RECTANGLE;
        }
        if (equalSides) {
            return RHOMBUS;
        }
        return ARBITRARY;
    }

    @Override
    public String toString() {
        return "(" + x1 + "; " + y1 + ") (" + x2 + "; " + y2 + ") ("
                + x3 + "; " + y3 + ") (" + x4 + "; " + y4 + ")"
                + "  S = " + String.format("%.4f", area())
                + ", P = " + String.format("%.4f", perimeter());
    }
}

public class QuadrilateralDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Quadrilateral> list = new ArrayList<Quadrilateral>();

        System.out.print("Скільки чотирикутників ввести? ");
        int n = Integer.parseInt(reader.readLine().trim());

        for (int i = 1; i <= n; i++) {
            System.out.println("Чотирикутник " + i + ": введіть x1 y1 x2 y2 x3 y3 x4 y4");
            String[] p = reader.readLine().trim().split("\\s+");
            Quadrilateral q = new Quadrilateral(
                    Double.parseDouble(p[0]), Double.parseDouble(p[1]),
                    Double.parseDouble(p[2]), Double.parseDouble(p[3]),
                    Double.parseDouble(p[4]), Double.parseDouble(p[5]),
                    Double.parseDouble(p[6]), Double.parseDouble(p[7]));
            list.add(q);
        }

        String[] names = {"Квадрат", "Прямокутник", "Ромб", "Довільний"};

        for (int t = 0; t < 4; t++) {
            int count = 0;
            Quadrilateral maxArea = null;
            Quadrilateral minArea = null;
            Quadrilateral maxPer = null;
            Quadrilateral minPer = null;

            for (Quadrilateral q : list) {
                if (q.getType() != t) {
                    continue;
                }
                count++;
                if (maxArea == null || q.area() > maxArea.area()) maxArea = q;
                if (minArea == null || q.area() < minArea.area()) minArea = q;
                if (maxPer == null || q.perimeter() > maxPer.perimeter()) maxPer = q;
                if (minPer == null || q.perimeter() < minPer.perimeter()) minPer = q;
            }

            System.out.println("\n=== " + names[t] + ": " + count + " шт. ===");
            if (count == 0) {
                System.out.println("Об'єктів цього типу немає");
            } else {
                System.out.println("Найбільша площа:   " + maxArea);
                System.out.println("Найменша площа:    " + minArea);
                System.out.println("Найбільший периметр: " + maxPer);
                System.out.println("Найменший периметр:  " + minPer);
            }
        }
    }
}
