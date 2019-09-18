package com.saravan.mastercard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode
@Log4j2
@NoArgsConstructor
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;


    @Transient
    @JsonIgnore
    private Cart cart;


    private String itemName;
    private BigDecimal itemPrice;

    @JsonIgnore
    private Boolean promo1;
    @JsonIgnore
    private Boolean promo2;

    private String promotion;

    private Integer counts;
    private BigDecimal totalPrice = new BigDecimal(0);

    private BigDecimal discountedPrice;

    @JsonIgnore
    @ManyToOne
    private Order order;

    public Bill(Cart cart) {

        this.cart = cart;
        Item item = cart.getItem();
        Promotion promotion = item.getPromotion();

        this.itemName = item.getItemName();
        this.itemPrice = item.getItemPrice();

        this.promo1 = promotion.getPromo1();
        this.promo2 = promotion.getPromo2();

        if (this.promo1) {
            this.promotion = "promo 1";
        }
        else if (this.promo2) {
            this.promotion = "promo 2";
        } else {
            this.promotion = ".     .";
        }

        this.counts = cart.getCounts();


    }

    @Override
    public String toString() {

        String muting;
        if (order != null ) {
            muting = ", order=" + order.getOrderId() +
                    ", totalSum=" + order.getTotalSum() ;
        }
        else {
            muting = ", order=null";
        }

        return "Bill{" +
                "billId=" + billId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", promo1=" + promo1 +
                ", promo2=" + promo2 +
                ", promotion='" + promotion + '\'' +
                ", counts=" + counts +
                ", totalPrice=" + totalPrice +
                ", discountedPrice=" + discountedPrice +
                muting +
                '}';
    }
}
