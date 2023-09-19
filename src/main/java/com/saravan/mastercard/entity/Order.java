package com.saravan.mastercard.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List< Bill > listBills = new ArrayList<>();

    public void addBill(Item item, Integer counts) {
        if (item == null || counts < 1)  return;

        Bill bill = new Bill(item, counts);
        addBill(bill);

    }



    public void addBill(Bill bill) {
        if  (listBills.contains(bill)) {

            listBills.stream().filter(f -> f.equals(bill)).forEach(f -> {

                f.setQty( f.getQty() + 1);

            });
        }
        else {
            listBills.add(bill);
        }
    }

    public void recalc() {
        totalSum = new BigDecimal(0);
        listBills.forEach(f -> {
            totalSum = totalSum.add(f.getTotal());
        });
    }


    @Override
    public String toString() {

        String muting;
        if (listBills != null ) {
            muting = ", listBills=" + listBills ;
        }
        else {
            muting = ", listBills=null";
        }

        return "Order{" +
                "orderId=" + orderId +
                ", totalSum=" + totalSum +
                muting +
                '}';
    }
}
