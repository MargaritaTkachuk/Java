package lab03;

import java.util.Scanner;

public class MinDigits {
    public int digits(long x) {
        if (x < 0) {
            x = -x;
        }
        int count = 1;
        while (x >= 10) {
            x /= 10;
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MinDigits md = new MinDigits();

        if (!scanner.hasNextInt()) {
            System.out.println("Некоректна довжина масиву!");
            return;
        }
        int n = scanner.nextInt();
        if (n < 1) {
            System.out.println("Некоректна довжина масиву!");
            return;
        }

        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }

        int min = md.digits(a[0]);
        for (int i = 1; i < n; i++) {
            int d = md.digits(a[i]);
            if (d < min) {
                min = d;
            }
        }

        System.out.println("Мінімальна кількість цифр: " + min);
        System.out.print("Числа:");
        for (int i = 0; i < n; i++) {
            if (md.digits(a[i]) == min) {
                System.out.print(" " + a[i]);
            }
        }
        System.out.println();
    }
}
