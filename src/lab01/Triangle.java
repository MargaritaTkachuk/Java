package lab01;

import java.util.Scanner;

public class Triangle {
    public double length(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double area(double a, double b, double c) {
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public static void main(String[] args) {
        Triangle t = new Triangle();
        Scanner scanner = new Scanner(System.in);

        double ax = scanner.nextDouble();
        double ay = scanner.nextDouble();
        double bx = scanner.nextDouble();
        double by = scanner.nextDouble();
        double cx = scanner.nextDouble();
        double cy = scanner.nextDouble();

        double ab = t.length(ax, ay, bx, by);
        double bc = t.length(bx, by, cx, cy);
        double ca = t.length(cx, cy, ax, ay);

        System.out.println("Площа трикутника: " + (ab + bc + ca));
        System.out.println("Площа трикутника: " + t.area(ab, bc, ca));

        // Тестовий варіант: a = 3, b = c = 3.5 + 3 * 2^(-111)
        double a = 3;
        double b = 3.5 + 3 * Math.pow(2, -111);
        double c = b;
        System.out.println("Тест: периметр = " + (a + b + c));
        System.out.println("Тест: площа = " + t.area(a, b, c));
    }
}