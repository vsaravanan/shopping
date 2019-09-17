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
public class Bill implements Serializable {

    // not used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;


    @ManyToOne
    private Cart cart;



}
