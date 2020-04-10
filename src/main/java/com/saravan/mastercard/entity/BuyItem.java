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

    public BuyItem(String itemName) {
        this.itemName = itemName;
        this.counts = 1;
    }

    public BuyItem(String itemName, Integer counts) {
        this.itemName = itemName;
        this.counts = counts;
    }


}
