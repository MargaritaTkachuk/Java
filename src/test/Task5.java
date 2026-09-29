package test;

import java.util.Scanner;

public class Task5 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("x: ");
        double x = scanner.nextDouble();

        // x^4+x^3+x^2+x+1 = ((((1)*x+1)*x+1)*x+1)*x+1
        double y = 1;
        y = y * x + 1;
        y = y * x + 1;
        y = y * x + 1;
        y = y * x + 1;

        System.out.println("y = " + y);

        scanner.close();
    }
}
