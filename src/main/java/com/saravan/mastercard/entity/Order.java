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


    @OneToMany(mappedBy="order")
    private List<Bill> bill;

    @Override
    public String toString() {

        String muting;
        if (bill != null ) {
            muting = ", bill=" + bill ;
        }
        else {
            muting = ", bill=null";
        }

        return "Order{" +
                "orderId=" + orderId +
                ", totalSum=" + totalSum +
                muting +
                '}';
    }
}
