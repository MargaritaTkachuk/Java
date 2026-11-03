package com.flowershop.flower;

/**
 * Ромашка.
 */
public class Chamomile extends Flower {

    public Chamomile(int price, int stemLength, int freshness) {
        super(price, stemLength, freshness);
    }

    @Override
    public String getTypeName() {
        return "Ромашка";
    }
}