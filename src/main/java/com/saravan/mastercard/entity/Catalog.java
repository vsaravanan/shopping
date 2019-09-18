package com.saravan.mastercard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Log4j2
public class Catalog {

    private String itemName;
    private BigDecimal itemPrice;
    private Boolean promo1;
    private Boolean promo2;

    @JsonIgnore
    private Item item;
    @JsonIgnore
    private Promotion promotion;

    public Catalog(String itemName, BigDecimal itemPrice, Boolean promo1, Boolean promo2) {

        this.item = new Item(itemName, itemPrice);
        this.promotion = new Promotion(this.item, promo1, promo2);
        this.item.setPromotion(this.promotion);

        log.info("new item added to the catalog " + item);



    }

    public Catalog(String itemName, BigDecimal itemPrice) {

        this(itemName, itemPrice, false, false);

    }
}
