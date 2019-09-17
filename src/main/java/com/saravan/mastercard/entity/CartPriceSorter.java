package com.saravan.mastercard.entity;

import java.util.Comparator;

public class CartPriceSorter implements Comparator<Cart> {

    @Override
    public  int compare(Cart c1, Cart c2) {
        return c2.getItem().getItemPrice().compareTo(c1.getItem().getItemPrice());
    }
}
