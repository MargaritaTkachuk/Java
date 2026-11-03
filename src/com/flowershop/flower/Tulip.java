package com.flowershop.flower;

/**
 * Тюльпан.
 */
public class Tulip extends Flower {

    public Tulip(int price, int stemLength, int freshness) {
        super(price, stemLength, freshness);
    }

    @Override
    public String getTypeName() {
        return "Тюльпан";
    }
}