package com.flowershop.flower;

/**
 * Базовий клас для всіх квітів.
 */
public abstract class Flower {

    private final int price;
    private final int stemLength;
    private final int freshness;

    /**
     * Створює квітку.
     *
     * @param price ціна в гривнях
     * @param stemLength довжина стебла в сантиметрах
     * @param freshness рівень свіжості від 1 до 10
     */
    protected Flower(int price, int stemLength, int freshness) {
        this.price = price;
        this.stemLength = stemLength;
        this.freshness = freshness;
    }

    /**
     * Повертає назву виду квітки.
     */
    public abstract String getTypeName();

    public int getPrice() {
        return price;
    }

    public int getStemLength() {
        return stemLength;
    }

    public int getFreshness() {
        return freshness;
    }

    @Override
    public String toString() {
        return getTypeName() + ": " + price + " грн, стебло "
                + stemLength + " см, свіжість " + freshness + "/10";
    }
}