package lab03;

import java.util.Random;
import java.util.Scanner;

public class Matrix {
    private int n;
    private int[][] a;
    private Scanner scanner;

    public Matrix(int n, Scanner scanner) {
        this.n = n;
        this.a = new int[n][n];
        this.scanner = scanner;
    }

    public void fillRandom() {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = random.nextInt(2 * n + 1) - n;
            }
        }
    }

    public void fillConsole() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean ok = false;
                while (!ok) {
                    System.out.print("a[" + i + "][" + j + "] = ");
                    if (!scanner.hasNextInt()) {
                        scanner.next();
                        System.out.println("Потрібне ціле число.");
                    } else {
                        int value = scanner.nextInt();
                        if (value < -n || value > n) {
                            System.out.println("Значення має бути від " + (-n) + " до " + n + ".");
                        } else {
                            a[i][j] = value;
                            ok = true;
                        }
                    }
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(String.format("%4d", a[i][j]));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("n = ");
        if (!scanner.hasNextInt()) {
            System.out.println("Число не натуральне!");
            return;
        }
        int n = scanner.nextInt();
        if (n < 1) {
            System.out.println("Число не натуральне!");
            return;
        }

        System.out.print("Заповнити: 1 - випадково, 2 - з консолі: ");
        int mode = scanner.hasNextInt() ? scanner.nextInt() : 0;

        Matrix m = new Matrix(n, scanner);
        if (mode == 1) {
            m.fillRandom();
        } else if (mode == 2) {
            m.fillConsole();
        } else {
            System.out.println("Невірний вибір.");
            return;
        }

        System.out.println("Матриця:");
        m.print();
    }
}
