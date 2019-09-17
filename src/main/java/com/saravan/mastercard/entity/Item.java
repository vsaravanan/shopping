package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Item {



    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;


    @Id
    @Column(unique = true)
    private String itemName;

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

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private Promotion promotion;

    private BigDecimal itemPrice;

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
                "itemId=" + itemId +
                ", itemName='" + itemName +
                ", itemPrice=" + itemPrice +
                strPromotion +
                "}";
    }


    public Item(String itemName, BigDecimal itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

}
