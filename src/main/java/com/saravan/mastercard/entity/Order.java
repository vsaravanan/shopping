package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="SOrder")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private BigDecimal totalSum = new BigDecimal(0);

    private String CustomerName;

    @OneToMany(mappedBy="order")
    private List<Cart> carts;

    @Transient
    private List<OrderItem> orderItems;

}
