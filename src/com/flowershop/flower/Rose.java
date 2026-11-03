package com.flowershop.flower;

/**
 * Троянда. Окрім спільних властивостей має колір.
 */
public class Rose extends Flower {

    private final String color;

    public Rose(String color, int price, int stemLength, int freshness) {
        super(price, stemLength, freshness);
        this.color = color;
    }

    @Override
    public String getTypeName() {
        return "Троянда (" + color + ")";
    }
}
