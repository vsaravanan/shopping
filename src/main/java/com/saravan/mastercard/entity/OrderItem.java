package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Log4j2
public class OrderItem {

    private String itemName;
    private Integer counts;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal total;

    public OrderItem(String itemName, Integer counts, BigDecimal originalPrice, BigDecimal discountedPrice, BigDecimal total) {
        this.itemName = itemName;
        this.counts = counts;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.total = total;
    }
}

