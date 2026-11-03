package lab02;
import java.util.Scanner;

public class kr2_4 {
    public long loop(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public long recursive(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * recursive(n - 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            System.out.println("Число не натуральне!");
            return;
        }

        int n = scanner.nextInt();
        if (n < 1) {
            System.out.println("Число не натуральне!");
            return;
        }

        kr2_4 f = new kr2_4();
        System.out.println("Цикл: " + n + "! = " + f.loop(n));
        System.out.println("Рекурсія: " + n + "! = " + f.recursive(n));
    }
}
