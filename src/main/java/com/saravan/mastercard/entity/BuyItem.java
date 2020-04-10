package com.saravan.mastercard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Log4j2
public class BuyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyId;

    private String itemName;
    private Integer counts;

//    @OneToOne(fetch = FetchType.LAZY)
    @OneToOne
//    @MapsId
    @JoinColumn(name = "itemName", insertable = false, updatable =  false, nullable = true)
    private Item item = null;

    public BuyItem(String itemName) {
        this.itemName = itemName;
        this.counts = 1;
    }

    public BuyItem(String itemName, Integer counts) {
        this.itemName = itemName;
        this.counts = counts;
    }


}
