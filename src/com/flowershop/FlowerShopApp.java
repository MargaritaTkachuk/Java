package com.flowershop;

import com.flowershop.bouquet.Accessory;
import com.flowershop.bouquet.Bouquet;
import com.flowershop.flower.Chamomile;
import com.flowershop.flower.Flower;
import com.flowershop.flower.Rose;
import com.flowershop.flower.Tulip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Консольний застосунок "Квіткарня".
 */
public class FlowerShopApp {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Збираємо букет
        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(new Rose("червона", 50, 60, 9));
        bouquet.addFlower(new Rose("біла", 55, 70, 6));
        bouquet.addFlower(new Tulip(25, 35, 8));
        bouquet.addFlower(new Tulip(25, 40, 4));
        bouquet.addFlower(new Chamomile(15, 30, 10));
        bouquet.addAccessory(new Accessory("Обгортка", 30));
        bouquet.addAccessory(new Accessory("Стрічка", 10));

        boolean running = true;
        while (running) {
            System.out.println("\n1 - Показати букет");
            System.out.println("2 - Сортувати за свіжістю");
            System.out.println("3 - Знайти за довжиною стебла");
            System.out.println("0 - Вихід");
            System.out.print("Ваш вибір: ");

            int choice = Integer.parseInt(reader.readLine().trim());

            switch (choice) {
                case 1:
                    for (Flower flower : bouquet.getFlowers()) {
                        System.out.println(flower);
                    }
                    for (Accessory accessory : bouquet.getAccessories()) {
                        System.out.println(accessory);
                    }
                    System.out.println("Вартість букета: "
                            + bouquet.getTotalPrice() + " грн");
                    break;
                case 2:
                    bouquet.sortByFreshness();
                    System.out.println("Букет відсортовано за свіжістю");
                    break;
                case 3:
                    System.out.print("Мінімальна довжина стебла, см: ");
                    int min = Integer.parseInt(reader.readLine().trim());
                    System.out.print("Максимальна довжина стебла, см: ");
                    int max = Integer.parseInt(reader.readLine().trim());

                    List<Flower> found = bouquet.findByStemLength(min, max);
                    if (found.isEmpty()) {
                        System.out.println("Нічого не знайдено");
                    }
                    for (Flower flower : found) {
                        System.out.println(flower);
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Невірний пункт меню");
                    break;
            }
        }
    }
}