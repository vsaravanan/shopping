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
@EqualsAndHashCode(of = {"itemName", "sellingPrice"})
@Log4j2
@NoArgsConstructor
public class Bill implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @JsonIgnore
    @ManyToOne
    private Order order;

    @ManyToOne
    @JsonIgnore
    private Item item;

    private String itemName;
    private BigDecimal itemPrice;

    private String promotion;


    private BigDecimal sellingPrice;

    private Integer qty = 1;

    private BigDecimal total = new BigDecimal(0);

    private void recalc() {
        this.total = sellingPrice.multiply(new BigDecimal(qty));
        if (order != null) {
            order.recalc();
        }
    }
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
        recalc();
    }

    public void setQty(Integer qty) {
        this.qty = qty;
        recalc();
    }

    public Bill(Item item, Integer counts) {

        this.item = item;
        this.qty = counts;


        this.itemName = item.getItemName();
        this.itemPrice = item.getItemPrice();
        this.sellingPrice = this.itemPrice;

        updatePromo();


    }
    public void updatePromo() {

        Promotion promotion = item.getPromotion();

        if (promotion.getPromo1()) {
            this.promotion = "promo 1";
        }
        else if (promotion.getPromo2()) {
            this.promotion = "promo 2";

        } else {
            this.promotion = ".     .";

        }

    }


    @Override
    public String toString() {

        String discountedMsg = (sellingPrice != null && sellingPrice.compareTo(itemPrice) < 0 ) ? promotion : "";

        String orderId = order != null && order.getOrderId() != null ? String.valueOf(order.getOrderId()) : "";

        return String.format("Order %s : Item %s : qty : %d * $ %10.2f = %10.2f : %s",
                orderId,
                itemName,
                qty,
                sellingPrice,
                total,
                discountedMsg
        );


    }
}
