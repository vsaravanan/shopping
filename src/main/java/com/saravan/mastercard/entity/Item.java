package com.saravan.mastercard.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Item {


    @Id
    @Column(unique = true)
    private String itemName;
    private BigDecimal itemPrice;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = true) // true

    private Promotion promotion;

    public Item(String itemName, BigDecimal itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void setPromotion(Promotion promotion) {
        if (promotion == null) {
            if (this.promotion != null) {
                this.promotion.setItem(null);
            }
        }
        else {
            promotion.setItem (this);
        }
        this.promotion = promotion;

    }

    @Override
    public String toString() {
        String strPromotion;
        if (promotion != null ) {
            strPromotion = ", promotion=" + promotion;
        }
        else {
            strPromotion = ", promotion=null";
        }

        return "Item{" +
                "  itemName='" + itemName +
                ", itemPrice=" + itemPrice +
                strPromotion +
                "}";
    }



}
