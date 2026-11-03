package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

/**
 * Комплексне число. Незмінний клас: після створення не змінюється.
 */
class ComplexNumber {

    private final double re;
    private final double im;

    public ComplexNumber() {
        this(0, 0);
    }

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    /**
     * Повертає суму цього числа й числа other.
     */
    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(re + other.re, im + other.im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexNumber other = (ComplexNumber) o;
        return Double.compare(re, other.re) == 0
                && Double.compare(im, other.im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    @Override
    public String toString() {
        String sign = im >= 0 ? " + " : " - ";
        return "(" + re + sign + Math.abs(im) + "i)";
    }
}

/**
 * Поліном з комплексними коефіцієнтами.
 * coefficients[i] це коефіцієнт при x^i.
 */
class Polynomial {

    private final ComplexNumber[] coefficients;

    public Polynomial() {
        this(new ComplexNumber[] {new ComplexNumber()});
    }

    public Polynomial(ComplexNumber[] coefficients) {
        this.coefficients = coefficients.clone();
    }

    public int getDegree() {
        return coefficients.length - 1;
    }

    public ComplexNumber getCoefficient(int power) {
        return coefficients[power];
    }

    /**
     * Повертає суму цього полінома й полінома other.
     */
    public Polynomial add(Polynomial other) {
        int length = Math.max(coefficients.length, other.coefficients.length);
        ComplexNumber[] result = new ComplexNumber[length];

        for (int i = 0; i < length; i++) {
            ComplexNumber a = i < coefficients.length
                    ? coefficients[i] : new ComplexNumber();
            ComplexNumber b = i < other.coefficients.length
                    ? other.coefficients[i] : new ComplexNumber();
            result[i] = a.add(b);
        }
        return new Polynomial(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Polynomial other = (Polynomial) o;
        return Arrays.equals(coefficients, other.coefficients);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coefficients);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result + coefficients[i];
            if (i == 1) {
                result = result + "x";
            } else if (i > 1) {
                result = result + "x^" + i;
            }
            if (i > 0) {
                result = result + " + ";
            }
        }
        return result;
    }
}

public class PolynomialDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Скільки поліномів m? ");
        int m = Integer.parseInt(reader.readLine().trim());

        Polynomial[] polynomials = new Polynomial[m];

        for (int k = 0; k < m; k++) {
            System.out.print("Поліном " + (k + 1) + ": степінь n = ");
            int n = Integer.parseInt(reader.readLine().trim());

            System.out.println("Введіть " + (n + 1)
                    + " пар (Re Im) коефіцієнтів, починаючи з вільного члена:");
            String[] p = reader.readLine().trim().split("\\s+");

            ComplexNumber[] coefficients = new ComplexNumber[n + 1];
            for (int i = 0; i <= n; i++) {
                coefficients[i] = new ComplexNumber(
                        Double.parseDouble(p[2 * i]),
                        Double.parseDouble(p[2 * i + 1]));
            }
            polynomials[k] = new Polynomial(coefficients);
        }

        System.out.println("\n=== Поліноми ===");
        for (int k = 0; k < m; k++) {
            System.out.println("P" + (k + 1) + "(x) = " + polynomials[k]);
        }

        Polynomial sum = new Polynomial();
        for (int k = 0; k < m; k++) {
            sum = sum.add(polynomials[k]);
        }

        System.out.println("\nСума: " + sum);
    }
}
