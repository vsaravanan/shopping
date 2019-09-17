package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Promotion implements Serializable {



    @Id
    @Column(unique = true)
    private String itemName;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    //@JoinColumn(name = "itemName")
    private Item item;

    public Promotion(Item item, Boolean promo1, Boolean promo2) {
        this.item = item;
        this.itemName = this.item.getItemName();
        this.promo1 = promo1;
        this.promo2 = promo2;
    }

    private Boolean promo1;
    private Boolean promo2;


    @Override
    public String toString() {

        String strItem;
        if (item != null ) {
            strItem = ", itemName=" + item.getItemName();
        }
        else {
            strItem = ", item=null";
        }

        return "Promotion{" +
                strItem +
                ", promo1=" + promo1 +
                ", promo2=" + promo2 +
                "}";

    }
}
