package com.saravan.mastercard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Log4j2
public class Shopping {

    Order order;
    List<Cart> carts = new ArrayList<>();


    public Shopping(Order order) {
        this.order = order;
    }

    public Cart addCart(Item item, Integer counts) {
        Cart cart = new Cart(item, counts);
        carts.add(cart);
        return cart;
    }



}
