package com.saravan.mastercard.service;

import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.CartRepo;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.repo.OrderRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ShoppingService {

//    private Integer itemId;
//    private String itemName;
//    private BigDecimal itemPrice;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    ItemRepo itemRepo;

    BigDecimal halfprice = new BigDecimal(0.5);
    BigDecimal zeroprice = new BigDecimal(0);

    public Shopping newOrder() {
        Order order = new Order();
        Shopping shopping = new Shopping(order);
        return shopping;
    }

    public Item findById(String itemName) {
        return itemRepo.findById(itemName).orElse(null);
    }

    public Order checkout(Shopping shopping) {
        Order order = orderRepo.saveAndFlush(shopping.getOrder());

        List<OrderItem> listOrderItems = new ArrayList<>();

        // filter promo2 items
        List<Cart> cartsPromo2 = shopping.getCarts().stream()
                .filter(r -> r.getItem().getPromotion().getPromo2()).collect(Collectors.toList());

        Collections.sort(cartsPromo2, new CartPriceSorter());


        // loop promo2 items
        int promo2count = 0;
        for (Cart cart : cartsPromo2) {
            BigDecimal newPrice = new BigDecimal(0);

            cart.setOrder(order);
            Item item = cart.getItem();
            BigDecimal itemPrice = item.getItemPrice();
            int cartIndex = 0;

            while (cartIndex < cart.getCounts()) {
                promo2count++;
                cartIndex++;
                if ( promo2count % 3 == 0) {
                    newPrice = zeroprice;
                }
                else {
                    newPrice = itemPrice;
                }
                log.info("{} promo2 newPrice : {}", item.getItemName(),  newPrice);
                cart.setTotalPrice(cart.getTotalPrice().add(newPrice));
            }
            order.setTotalSum( order.getTotalSum().add(cart.getTotalPrice()));
            log.info("{} : cart.getTotalPrice() : {}, order.getTotalSum() : {}", item.getItemName(), cart.getTotalPrice(), order.getTotalSum());
            cartRepo.saveAndFlush(cart);

            OrderItem orderItem = new OrderItem(
                    item.getItemName(),
                    cart.getCounts(),
                    item.getItemPrice(),
                    newPrice,
                    cart.getTotalPrice()
            );

            listOrderItems.add(orderItem);


        }

        // filter other than promo2 items
        List<Cart> cartsRest = shopping.getCarts().stream()
                .filter(r -> ! r.getItem().getPromotion().getPromo2()).collect(Collectors.toList());

        for (Cart cart : cartsRest) {
            BigDecimal newPrice =  new BigDecimal(0);
            cart.setOrder(order);
            Item item = cart.getItem();
            BigDecimal itemPrice = item.getItemPrice();
            Promotion promo = item.getPromotion();
            if (promo.getPromo1()) {
                BigDecimal promo1priceAfterDiscount = itemPrice.multiply(halfprice);
                int promo1count = 0;
                while (promo1count < cart.getCounts()) {
                    promo1count++;
                    if ( promo1count % 2 == 0) {
                        newPrice = promo1priceAfterDiscount;
                    }
                    else {
                        newPrice = itemPrice;
                    }
                    log.info("{} promo1 newPrice : {}", item.getItemName(),  newPrice);
                    cart.setTotalPrice(cart.getTotalPrice().add(newPrice));
                }

            }
            else {
                cart.setTotalPrice(itemPrice.multiply(new BigDecimal(cart.getCounts())));
                log.info("{} no promo newPrice : {}", item.getItemName(),  cart.getTotalPrice());
            }

            order.setTotalSum( order.getTotalSum().add(cart.getTotalPrice()));
            log.info("{} : cart.getTotalPrice() : {}, order.getTotalSum() : {}", item.getItemName(), cart.getTotalPrice(), order.getTotalSum());
            cartRepo.saveAndFlush(cart);

            OrderItem orderItem = new OrderItem(
                    item.getItemName(),
                    cart.getCounts(),
                    item.getItemPrice(),
                    newPrice,
                    cart.getTotalPrice()
            );

            listOrderItems.add(orderItem);

        }

        order.setOrderItems(listOrderItems);

        orderRepo.saveAndFlush(order);


        return order;

    }



}
