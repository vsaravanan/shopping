package com.saravan.mastercard.entity;

import java.util.Comparator;

public class BillPriceSorter implements Comparator< Bill > {

    @Override
    public  int compare(Bill c1, Bill c2) {
        return c2.getItemPrice().compareTo(c1.getItemPrice());
    }
}
