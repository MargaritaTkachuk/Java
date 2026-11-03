package lab01;
import java.util.Scanner;

public class kr1_4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double number = input.nextDouble();
        double result = Math.pow(number, 8);

        System.out.printf("%20.4f", result);
    }
}
