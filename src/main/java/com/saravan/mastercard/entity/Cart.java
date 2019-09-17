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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId; // seqNo

    @ManyToOne
    private Order order;

    @ManyToOne
    private Item item;
    private Integer counts;

    private BigDecimal totalPrice = new BigDecimal(0);

    public Cart(Item item, Integer counts) {
        this.item = item;
        this.counts = counts;
    }


}
