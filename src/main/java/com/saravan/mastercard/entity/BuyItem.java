package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Log4j2
public class BuyItem {

    private String itemName;
    private Integer counts;
    private Item item;
    private Cart cart;

    public BuyItem(Item item, Integer counts) {
        this.itemName = item.getItemName();
        this.counts = counts;

        Cart cart = new Cart(item, counts);

    }



}
