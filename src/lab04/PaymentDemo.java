package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Платіж: покупка з кількох товарів.
 */
class Payment {

    private final String customer;
    private final ArrayList<Product> products = new ArrayList<Product>();

    public Payment(String customer) {
        this.customer = customer;
    }

    /**
     * Внутрішній клас: один товар у покупці.
     * Прив'язаний до конкретного платежу.
     */
    public class Product {

        private final String name;
        private final double price;
        private final int quantity;

        public Product(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            // доступ до списку зовнішнього класу: товар сам додається в свій платіж
            products.add(this);
        }

        public double getCost() {
            return price * quantity;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%-12s %8.2f x %-3d = %9.2f",
                    name, price, quantity, getCost());
        }
    }

    /**
     * Загальна сума покупки.
     */
    public double getTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getCost();
        }
        return total;
    }

    /**
     * Друкує чек.
     */
    public void print() {
        System.out.println("=== Чек. Покупець: " + customer + " ===");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println(String.format(Locale.US, "Разом до сплати: %.2f грн",
                getTotal()));
    }
}

public class PaymentDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ім'я покупця: ");
        Payment payment = new Payment(reader.readLine().trim());

        System.out.print("Скільки товарів? ");
        int n = Integer.parseInt(reader.readLine().trim());

        for (int i = 1; i <= n; i++) {
            System.out.println("Товар " + i + ": назва, ціна, кількість (через пробіл)");
            String[] p = reader.readLine().trim().split("\\s+");

            // створення внутрішнього класу через об'єкт зовнішнього
            payment.new Product(p[0], Double.parseDouble(p[1]),
                    Integer.parseInt(p[2]));
        }

        System.out.println();
        payment.print();
    }
}
