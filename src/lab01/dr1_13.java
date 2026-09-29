package lab01;
import java.util.Scanner;

public class dr1_13 {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.println("Input A:");
        double a = input.nextDouble();

        System.out.println("Input B:");
        double b = input.nextDouble();

        System.out.println("Input C:");
        double c = input.nextDouble();

        if (a + b <= c || b + c <= a || a + c <= b) {
            System.out.println("invalid triangle");
            return;
        }

        double p = (a + b + c) / 2;
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));

        double heightA = (2 * s) / a;
        double heightB = (2 * s) / b;
        double heightC = (2 * s) / c;

        double medianA = 0.5 * Math.sqrt(2*b*b + 2*c*c - a*a);
        double medianB = 0.5 * Math.sqrt(2*a*a + 2*c*c - b*b);
        double medianC = 0.5 * Math.sqrt(2*a*a + 2*b*b - c*c);

        double bisectorA = 2 * Math.sqrt(b*c*p*(p-a)) / (b+c);
        double bisectorB = 2 * Math.sqrt(a*c*p*(p-b)) / (a+c);
        double bisectorC = 2 * Math.sqrt(a*b*p*(p-c)) / (a+b);

        System.out.println("Height A: " + heightA);
        System.out.println("Height B: " + heightB);
        System.out.println("Height C: " + heightC);

        System.out.println("Median A: " + medianA);
        System.out.println("Median B: " + medianB);
        System.out.println("Median C: " + medianC);

        System.out.println("Bisector A: " + bisectorA);
        System.out.println("Bisector B: " + bisectorB);
        System.out.println("Bisector C: " + bisectorC);
    }
}
