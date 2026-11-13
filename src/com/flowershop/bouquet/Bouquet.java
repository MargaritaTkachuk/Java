package com.flowershop.bouquet;

import com.flowershop.flower.Flower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Букет: набір квітів та аксесуарів.
 */
public class Bouquet {

    private final List<Flower> flowers = new ArrayList<>();
    private final List<Accessory> accessories = new ArrayList<>();

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
    }

    /**
     * Рахує вартість букета: сума цін усіх квітів і аксесуарів.
     */
    public int getTotalPrice() {
        int total = 0;
        for (Flower flower : flowers) {
            total += flower.getPrice();
        }
        for (Accessory accessory : accessories) {
            total += accessory.getPrice();
        }
        return total;
    }

    /**
     * Сортує квіти за свіжістю: від найсвіжіших до найменш свіжих.
     */
    public void sortByFreshness() {
        flowers.sort(Comparator.comparingInt(Flower::getFreshness).reversed());
    }

    /**
     * Шукає квіти, довжина стебла яких лежить у діапазоні [min; max].
     */
    public List<Flower> findByStemLength(int min, int max) {
        List<Flower> result = new ArrayList<>();
        for (Flower flower : flowers) {
            if (flower.getStemLength() >= min
                    && flower.getStemLength() <= max) {
                result.add(flower);
            }
        }
        return result;
    }

    public List<Flower> getFlowers() {
        return Collections.unmodifiableList(flowers);
    }

    public List<Accessory> getAccessories() {
        return Collections.unmodifiableList(accessories);
    }
}