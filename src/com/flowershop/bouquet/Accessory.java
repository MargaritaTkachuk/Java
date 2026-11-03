package com.flowershop.bouquet;

/**
 * Аксесуар для букета (обгортка, стрічка тощо).
 */
public class Accessory {

    private final String name;
    private final int price;

    public Accessory(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + ": " + price + " грн";
    }
}