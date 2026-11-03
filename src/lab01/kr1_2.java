package lab01;

public class kr1_2 {
    public static void main(String[] args) {

        double num1 = Double.parseDouble(args[0]);
        double num2 = Double.parseDouble(args[1]);
        double num3 = Double.parseDouble(args[2]);

        System.out.println("Перше число: " + num1);
        System.out.println("Друге число: " + num2);
        System.out.println("Третє число: " + num3);

        double sum = num1 + num2 + num3;
        System.out.println("Сума: " + sum);
    }
}
